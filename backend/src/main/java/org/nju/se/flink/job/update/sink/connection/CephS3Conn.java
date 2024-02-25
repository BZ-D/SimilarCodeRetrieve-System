package org.nju.se.flink.job.update.sink.connection;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.util.List;

public class CephS3Conn {
    private AmazonS3 s3Client;

    private void initS3Client(String endpoint, String accessKey, String secretKey) {
        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public CephS3Conn(){
        // Ceph S3 配置信息
        String endpoint = "http://172.19.241.52:7480";
        String accessKey = "test1";
        String secretKey = "0DVG2lSU04j6ZEK3qiVAxTCn60rzm7HZPDOeF9NO";
        // 初始化 Amazon S3 客户端
        initS3Client(endpoint, accessKey, secretKey);
    }

    public void close(){
        s3Client.shutdown();
    }

    /**
     * 获取单个文件
     * @param bucketName 桶
     * @param objectKey 键
     * @return 文件内容
     */
    public String getFileFromS3(String bucketName, String objectKey) {
        try {
            // 创建 GetObjectRequest
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectKey);

            // 获取 S3Object
            S3Object s3Object = s3Client.getObject(getObjectRequest);

            // 获取 S3ObjectInputStream
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(objectInputStream))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append(System.lineSeparator());
                }

                return stringBuilder.toString();
            }
        } catch (IOException e) {
            System.out.println("下载文件时发生错误：" + e.getMessage());
            return null;
        }
    }

    /**
     * 上传单个文件
     * @param fileContent 文件内容
     * @param bucketName 桶
     * @param objectKey 键
     * @return true即成功，false即失败
     */
    public boolean upLoadFileToS3(String fileContent, String bucketName, String objectKey){
        try {
            // 将字符串内容转换为字节数组
            byte[] contentBytes = fileContent.getBytes();
            // 创建 ByteArrayInputStream 以便将字节数组包装成输入流
            InputStream inputStream = new ByteArrayInputStream(contentBytes);
            // 使用 Amazon S3 客户端上传输入流到指定的桶和键
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(contentBytes.length);
            s3Client.putObject(bucketName, objectKey, inputStream, meta);
//            System.out.println("上传文件成功！");
            return true;
        }catch (Exception e){
            System.out.println("上传文件时发生错误：" + e.getMessage());
            return false;
        }
    }

    /**
     * 删除单个文件
     * @param bucketName 桶
     * @param objectKey 键
     * @return true即成功，false即失败
     */
    public boolean deleteFileFromS3(String bucketName, String objectKey){
        try {
            s3Client.deleteObject(bucketName, objectKey);
            // 打印删除成功的消息
//            System.out.println("从 Ceph 存储集群删除对象成功！");
            // 返回 true 表示删除成功
            return true;
        }catch (Exception e){
            System.out.println("上传文件时发生错误：" + e.getMessage());
            return false;
        }
    }

    public List<Bucket> listBuckets(){
        return s3Client.listBuckets();
    }

    public Bucket createBucket(){
        return s3Client.createBucket("my-new-bucket");
    }

    public void deleteBucket(Bucket bucket){
        s3Client.deleteBucket(bucket.getName());
    }

    /**
     * 批量上传
     * @param localRepoRoot 目录
     * @param bucketName 目标桶
     * @return 目录下的java文件数量
     */
    public int uploadJavaFilesRecursive(String localRepoRoot, String bucketName) {
        File localRepo = new File(localRepoRoot);
        if (!localRepo.exists() || !localRepo.isDirectory()) {
            System.out.println("指定的本地仓库路径不存在或不是一个目录：" + localRepoRoot);
            return 0;
        }
        //获取仓库名称
        String repoName = localRepo.getName();
        int javaFileCount = uploadJavaFiles(localRepo, bucketName, repoName);
        System.out.println("Java文件上传完成，仓库："+localRepoRoot+"，该仓库java文件数："+javaFileCount);
        return javaFileCount;
    }

    public int uploadJavaFiles(File directory, String bucketName, String repoName) {
        File[] files = directory.listFiles();
        // 记录Java文件数量
        int javaFileCount = 0;
        try {
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归处理子目录
                        javaFileCount += uploadJavaFiles(file, bucketName, repoName);
                    } else if (file.getName().endsWith(".java")) {
                        javaFileCount++;
                        // 使用路径作为objectKey
                        int beginIndex = file.getPath().indexOf("/"+repoName+"/")+1;
                        String objectKey = file.getPath().substring(beginIndex);
                        s3Client.putObject(bucketName, objectKey, file);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("批量上传文件时发生错误：" + e.getMessage());
        }

        return javaFileCount;
    }

    public static void main(String[] args) throws IOException {
        CephS3Conn conn = new CephS3Conn();
        // 说明：
        // ceph存储时 键使用java文件在仓库中的路径
        // android、microservice、util 3个桶存放不同领域的java文件
//        String baseDir = "/Users/vincinx/Documents/0.lessons/data-3/";
//        String[] repoDirectories = {
//                "MAODS_Java","dataease","maven","gradle","ant","testng","findbugs","checkstyle","spotbugs","jenkins",
//                "lombok","swagger-core","generator-jhipster","flyway","logback","jacoco","mapstruct","jOOQ","liquibase","mockserver"
//        };
//        int repoFilenum=0;
//        // 遍历仓库目录列表
//        for (String repoDirectory : repoDirectories) {
//            repoFilenum += conn.uploadJavaFilesRecursive(baseDir+repoDirectory, "util");
//        }
//        System.out.println("该领域共上传 "+repoFilenum+"个文件");

        // test 获取单个文件
        String fileContent=conn.getFileFromS3("testbucket", "zulip-android/customlintjar/src/main/java/com/zulip/android/customlintrules/detectors/PrintStackTraceDetector.java");
        System.out.println(fileContent);

        // test 上传单个文件
        conn.upLoadFileToS3(fileContent+"_test","testbucket","zulip-android/customlintjar/src/main/java/com/zulip/android/customlintrules/detectors/PrintStackTraceDetector_test.java");
        fileContent=conn.getFileFromS3("testbucket", "zulip-android/customlintjar/src/main/java/com/zulip/android/customlintrules/detectors/PrintStackTraceDetector_test.java");
        System.out.println(fileContent);
        try {
            conn.deleteFileFromS3("testbucket", "zulip-android/customlintjar/src/main/java/com/zulip/android/customlintrules/detectors/PrintStackTraceDetector_test.java");
            fileContent=conn.getFileFromS3("testbucket", "zulip-android/customlintjar/src/main/java/com/zulip/android/customlintrules/detectors/PrintStackTraceDetector_test.java");
            System.out.println(fileContent);
        }catch (Exception e){
            System.out.println("删除成功");
        }
        conn.close();
    }
}
