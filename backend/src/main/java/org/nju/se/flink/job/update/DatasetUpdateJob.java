package org.nju.se.flink.job.update;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.nju.se.flink.job.update.entity.CommitFile;
import org.nju.se.flink.job.update.entity.CommitLog;
import org.nju.se.flink.job.update.entity.GithubRepository;
import org.nju.se.flink.job.update.function.CommitFileFilter;
import org.nju.se.flink.job.update.function.CommitLogCollector;
import org.nju.se.flink.job.update.helper.RequestHelper;
import org.nju.se.flink.job.update.sink.CommitSink;
import org.nju.se.flink.job.update.sink.FileSink;
import org.nju.se.flink.job.update.sink.RepositorySink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DatasetUpdateJob {

    private static final String querySql = "SELECT id, username, repo_name, repo_classification, latest_commit_id FROM code_repo_info WHERE id  = 1";

    public static void main(String[] args) {
        // 处理命令行参数
        ParameterTool params = ParameterTool.fromArgs(args);
        String jdbcUrl = params.get("mysql.url", "jdbc:mysql://172.29.7.168:32306/similar_code_retrieval?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true");
        String username = params.get("mysql.username", "root");
        String password = params.get("mysql.password", "rootpassword");
        String token = params.get("github.token", "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");

        // 设置flink执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);

        // 获取MySQL输入数据
        List<GithubRepository> repositorySet = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(querySql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                GithubRepository repository = new GithubRepository();
                repository.setId(resultSet.getLong("id"));
                repository.setUsername(resultSet.getString("username"));
                repository.setRepositoryName(resultSet.getString("repo_name"));
                repository.setTag(resultSet.getString("repo_classification"));
                repository.setLastCommitId(resultSet.getString("latest_commit_id"));
                repositorySet.add(repository);
            }
            connection.close();
            preparedStatement.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        DataStream<GithubRepository> repositoryStream = env
          .fromCollection(repositorySet)
          .name("Github Repository Stream");

        // 设置github api的访问token
        RequestHelper.setToken(token);

        // 获取所有仓库的commit信息
        DataStream<CommitLog> commitStream = repositoryStream
          .flatMap(new CommitLogCollector())
          .returns(CommitLog.class)
          .name("Commit Log Stream");

        // commit信息持久化
        commitStream
          .addSink(new CommitSink(jdbcUrl, username, password))
          .name("Commit Log Sink");

        // 获取所有commit的file信息
        DataStream<CommitFile> fileStream = commitStream
          .flatMap(new CommitFileFilter())
          .returns(CommitFile.class)
          .name("Commit File Stream");

        // file信息持久化，存储到ceph对象存储
        fileStream
          .addSink(new FileSink(jdbcUrl, username, password))
          .name("Commit File Sink");

        // repository的最新commit信息持久化
        commitStream
          .filter(CommitLog::isLatest)
          .addSink(new RepositorySink(jdbcUrl, username, password))
          .name("Repository Sink");

        try {
            env.execute();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
