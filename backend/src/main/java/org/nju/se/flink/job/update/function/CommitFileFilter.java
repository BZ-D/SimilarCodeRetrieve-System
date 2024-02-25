package org.nju.se.flink.job.update.function;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.nju.se.flink.job.update.entity.CommitFile;
import org.nju.se.flink.job.update.entity.CommitLog;
import org.nju.se.flink.job.update.helper.RequestHelper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class CommitFileFilter implements FlatMapFunction<CommitLog, CommitFile> {

    private final Set<String> fileSet;

    private static final String CONTENT_URL = "https://api.github.com/repos/%s/%s/contents/%s";

    public CommitFileFilter() {
        fileSet = new HashSet<>();
    }

    private String getFileContent(CommitFile commitFile) {
        String url = String.format(CONTENT_URL, commitFile.getUsername(), commitFile.getRepositoryName(), commitFile.getPath());
        String response = RequestHelper.get(url);
        JSONObject jFile = JSON.parseObject(response);
        if (jFile != null) {
            String content = jFile.getString("content").trim();
            return new String(Base64.getMimeDecoder().decode(content), StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public void flatMap(CommitLog commit, Collector<CommitFile> collector) {
        for (CommitFile commitFile : commit.getFiles()) {
            String key = commitFile.getUsername() + "/" + commitFile.getRepositoryName() + "/" + commitFile.getPath();
            if (!fileSet.contains(key)) {
                if (!"removed".equals(commitFile.getStatus())) {
                    String content = getFileContent(commitFile);
                    commitFile.setContent(content);
                }
                collector.collect(commitFile);
                fileSet.add(key);
            }
        }
    }
}
