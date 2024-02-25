package org.nju.se.flink.job.update.function;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.nju.se.flink.job.update.entity.CommitFile;
import org.nju.se.flink.job.update.entity.CommitLog;
import org.nju.se.flink.job.update.entity.GithubRepository;
import org.nju.se.flink.job.update.helper.RequestHelper;

import java.util.ArrayList;
import java.util.List;

public class CommitLogCollector implements FlatMapFunction<GithubRepository, CommitLog> {

    private static final String COMMITS_URL = "https://api.github.com/repos/%s/%s/commits?per_page=100&page=%d";

    private static final String COMMIT_URL = "https://api.github.com/repos/%s/%s/commits/%s?per_page=300&page=%d";

    @Override
    public void flatMap(GithubRepository repository, Collector<CommitLog> collector) throws Exception {
        int commitsPageNo = 1;
        while (true) {
            String commitsUrl = String.format(COMMITS_URL, repository.getUsername(), repository.getRepositoryName(), commitsPageNo);
            String response = RequestHelper.get(commitsUrl);
            JSONArray jCommits = JSON.parseArray(response);
            for (int i = 0; i < jCommits.size(); i++) {
                JSONObject jCommit = jCommits.getJSONObject(i);
                String commitId = jCommit.getString("sha");
                if (commitId.equals(repository.getLastCommitId())) {
                    break;
                }
                CommitLog commit = new CommitLog();
                commit.setRepository(repository);
                if (commitsPageNo == 1 && i == 0) {
                    commit.setLatest(true);
                }
                commit.setCommitId(commitId);
                List<CommitFile> commitFiles = new ArrayList<>();
                int commitPageNo = 1;
                while (true) {
                    String commitUrl = String.format(COMMIT_URL, commit.getUsername(), commit.getRepositoryName(), commitId, commitPageNo);
                    response = RequestHelper.get(commitUrl);
                    jCommit = JSON.parseObject(response);
                    if (commitPageNo == 1) {
                        commit.setMessage(jCommit.getJSONObject("commit").getString("message"));
                        commit.setAffectFileCount(jCommit.getJSONArray("files").size());
                        commit.setAddLineCount(jCommit.getJSONObject("stats").getInteger("additions"));
                        commit.setDeleteLineCount(jCommit.getJSONObject("stats").getInteger("deletions"));
                    }
                    JSONArray jFiles = jCommit.getJSONArray("files");
                    for (int j = 0; j < jFiles.size(); j++) {
                        JSONObject jFile = jFiles.getJSONObject(j);
                        String path = jFile.getString("filename");
                        if (path.endsWith(".java")) {
                            CommitFile commitFile = new CommitFile();
                            commitFile.setRepository(repository);
                            commitFile.setPath(path);
                            String status = jFile.getString("status");
                            if ("renamed".equals(status)) {
                                commitFile.setStatus("added");
                                CommitFile removedFile = new CommitFile();
                                removedFile.setRepository(repository);
                                removedFile.setPath(jFile.getString("previous_filename"));
                                removedFile.setStatus("removed");
                                commitFiles.add(removedFile);
                            } else {
                                commitFile.setStatus(status);
                            }
                            commitFiles.add(commitFile);
                        }
                    }
                    if (jFiles.size() < 300) {
                        break;
                    }
                    ++commitPageNo;
                }
                commit.setFiles(commitFiles);
                collector.collect(commit);
            }
            if (jCommits.size() < 100) {
                break;
            }
            ++commitsPageNo;
        }
    }
}
