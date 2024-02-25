package com.src.srcapi.util.obs;

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import com.src.srcapi.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
public class CustomOBSClient {
    private final ObsClient obsClient;

    private final String bucketName;

    public Optional<String> uploadObject(MultipartFile file){
        String objectKey = file.getOriginalFilename();
        try {
            InputStream codeFileInputStream = file.getInputStream();
            PutObjectResult result = obsClient.putObject(bucketName, objectKey, codeFileInputStream);
            return Optional.of(result.getObjectUrl());
        } catch (IOException e) {
            log.error("Upload code file error, fileName: {}", file.getOriginalFilename());
        }
        return Optional.empty();
    }

    public Optional<String> fetchObject(String objectKey){
        ObsObject object = obsClient.getObject(bucketName, objectKey);
        InputStream objectContent = object.getObjectContent();
        return Optional.of(StringUtils.getStringByInputStream(objectContent));
    }


    public CustomOBSClient(String bucketName, String endpoint, String ak, String sk) {
        obsClient = new ObsClient(ak, sk, endpoint);
        this.bucketName = bucketName;
    }

    public static void main(String[] args) {
        CustomOBSClient obsClient = new CustomOBSClient("similar-code-retrieve", "obs.cn-southwest-2.myhuaweicloud.com", "RZCQIDKJTZV4IXCPVTIO", "qPMPGo5jZa57zZVgTGvyytDEJDRsXZJ2PpI6x6H1");
        obsClient.fetchObject("DatasetUpdateJob.java");
    }
}
