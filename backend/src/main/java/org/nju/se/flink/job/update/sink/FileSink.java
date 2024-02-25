package org.nju.se.flink.job.update.sink;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.nju.se.flink.job.update.sink.connection.CephS3Conn;
import org.nju.se.flink.job.update.entity.CommitFile;
import org.nju.se.flink.job.update.entity.CommitFileMeta;
import org.nju.se.flink.job.update.sink.connection.ESConn;
import org.nju.se.flink.job.update.sink.serializer.SerializerVisitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileSink extends RichSinkFunction<CommitFile> {

    private final String jdbcUrl;

    private final String username;

    private final String password;

    private CephS3Conn cephConn;

    private ESConn esConn;

    private Connection mysqlConn;

    private static final String insertSql = "INSERT IGNORE INTO ast_record(`repo_id`, `code_tag`) VALUES (?, ?)";

    private static final String deleteSql = "DELETE FROM ast_record WHERE code_tag = ?";

    public FileSink(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        cephConn = new CephS3Conn();
        esConn = new ESConn();
        mysqlConn = DriverManager.getConnection(jdbcUrl, username, password);
    }

    @Override
    public void close() throws Exception {
        mysqlConn.close();
        esConn.close();
        cephConn.close();
    }

    @Override
    public void invoke(CommitFile commitFile, Context context) throws SQLException {
        String fileKey = commitFile.getUsername() + "/" + commitFile.getRepositoryName() + "/" + commitFile.getPath();
        String keyPrefix = fileKey.replace(".java", "");
        System.out.println(keyPrefix);
        String astKey = keyPrefix + ".ast";
        String esKey = keyPrefix.replace("/", "_");
        PreparedStatement preparedStatement;
        if (!"removed".equals(commitFile.getStatus())) {
            // 写入文件内容
            cephConn.upLoadFileToS3(commitFile.getContent(), commitFile.getTag(), fileKey);
            // file构建AST
            CompilationUnit cu = StaticJavaParser.parse(commitFile.getContent());
            SerializerVisitor serializer = new SerializerVisitor();
            // 遍历AST并序列化为字符串
            String ast = StringUtils.join(cu.accept(serializer, null).toArray(), "");
            // AST序列化、存到对象存储
            cephConn.upLoadFileToS3(ast, commitFile.getTag(), astKey);
            // 更新ES文档
            CommitFileMeta fileMeta = new CommitFileMeta();
            fileMeta.setTag(commitFile.getTag());
            fileMeta.setRepositoryId(commitFile.getRepositoryId());
            fileMeta.setClassList(cu.findAll(ClassOrInterfaceDeclaration.class).stream().map(n -> n.getName().getIdentifier()).collect(Collectors.toList()));
            fileMeta.setDependencyList(cu.findAll(ImportDeclaration.class).stream().map(n -> n.getName().getIdentifier()).collect(Collectors.toList()));
            fileMeta.setFieldList(cu.findAll(FieldDeclaration.class).stream().flatMap(n ->
              n.getVariables().stream().map(
                m -> m.getTypeAsString() + ":" + m.getNameAsString()
              )
            ).collect(Collectors.toList()));
            fileMeta.setMethodList(cu.findAll(MethodDeclaration.class).stream().map(
              n -> {
                  StringBuffer sb = new StringBuffer();
                  sb.append(n.getTypeAsString());
                  sb.append("/");
                  sb.append(n.getNameAsString());
                  sb.append("/");
                  n.getParameters().forEach(p -> {
                      sb.append(p.getTypeAsString() + ":" + p.getNameAsString());
                      sb.append(",");
                  });
                  sb.deleteCharAt(sb.length() - 1);
                  return sb.toString();
              })
              .collect(Collectors.toList())
            );
            esConn.updateFile(esKey, fileMeta);
            // 插入file记录
            preparedStatement = mysqlConn.prepareStatement(insertSql);
            preparedStatement.setLong(1, commitFile.getRepositoryId());
            preparedStatement.setString(2, keyPrefix);
        } else {
            // 删除文件和AST
            cephConn.deleteFileFromS3(commitFile.getTag(), fileKey);
            cephConn.deleteFileFromS3(commitFile.getTag(), astKey);
            // 删除ES文档
            esConn.deleteFile(esKey);
            // 删除file记录
            preparedStatement = mysqlConn.prepareStatement(deleteSql);
            preparedStatement.setString(1, keyPrefix);
        }
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
