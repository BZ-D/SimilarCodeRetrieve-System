package com.src.srcapi.util.ceph;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.*;
import java.util.Optional;

/**
 * 包装CephS3Conn中的getFileFromS3(String bucketName, String objectKey)接口
 * objectKey与uri含义相同
 * bucketName："android"、"microservice"、"util" 3个桶存放不同领域的java文件
 */
public class CephClient {
    private final AmazonS3 s3Client;

    public CephClient(String endpoint, String accessKey, String secretKey){
        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
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
    public Optional<String> getFileFromS3(String bucketName, String objectKey) {
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

                return Optional.of(stringBuilder.toString());
            }
        } catch (IOException e) {
            System.out.println("下载文件时发生错误：" + e.getMessage());
            return Optional.empty();
        }
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
        String fileContent=conn.getFileFromS3("microservice", "linzs148/java-test/DatasetUpdateJob.ast");
        System.out.println(fileContent);
//
//        // test 上传单个文件
//        conn.upLoadFileToS3(fileContent+"_test","testbucket","zulip-android/customlintjar/src/main/java/com/zulip/android/customlintrules/detectors/PrintStackTraceDetector_test.java");
//        fileContent=conn.getFileFromS3("testbucket", "zulip-android/customlintjar/src/main/java/com/zulip/android/customlintrules/detectors/PrintStackTraceDetector_test.java");
//        System.out.println(fileContent);
        conn.close();
    }
}
