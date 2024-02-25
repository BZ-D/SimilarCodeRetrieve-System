package org.nju.se.flink.job.update.sink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.nju.se.flink.job.update.entity.CommitLog;
import org.nju.se.flink.job.update.entity.GithubRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class RepositorySink extends RichSinkFunction<CommitLog> {

    private final String jdbcUrl;

    private final String username;

    private final String password;

    private static final String updateSql = "UPDATE code_repo_info SET latest_commit_id = ? WHERE id = ?";

    public RepositorySink(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public void invoke(CommitLog commitLog, Context context) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
        preparedStatement.setString(1, commitLog.getCommitId());
        preparedStatement.setLong(2, commitLog.getRepositoryId());
        preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
    }
}
