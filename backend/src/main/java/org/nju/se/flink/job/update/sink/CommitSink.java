package org.nju.se.flink.job.update.sink;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.nju.se.flink.job.update.entity.CommitLog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommitSink extends RichSinkFunction<CommitLog> {

    private final String jdbcUrl;

    private final String username;

    private final String password;

    private static final String insertSql = "INSERT IGNORE INTO commit_record(`repo_id`, `commit_id`, `commit_msg`, `affect_files_count`, `add_lines_count`, `minus_lines_count`) VALUES (?, ?, ?, ?, ?, ?)";

    public CommitSink(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public void invoke(CommitLog commitLog, Context context) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setLong(1, commitLog.getRepositoryId());
        preparedStatement.setString(2, commitLog.getCommitId());
        preparedStatement.setString(3, commitLog.getMessage());
        preparedStatement.setInt(4, commitLog.getAffectFileCount());
        preparedStatement.setInt(5, commitLog.getAddLineCount());
        preparedStatement.setInt(6, commitLog.getDeleteLineCount());
        preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
    }
}
