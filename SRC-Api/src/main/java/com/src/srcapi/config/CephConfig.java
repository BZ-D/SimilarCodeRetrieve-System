package com.src.srcapi.config;

import com.src.srcapi.util.ceph.CephClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CephConfig {
    @Value("${ceph.s3.endpoint}")
    private String endpoint;

    @Value("${ceph.s3.accessKey}")
    private String accessKey;

    @Value("${ceph.s3.secretKey}")
    private String secretKey;

    @Bean
    public CephClient cephClient(){
        return new CephClient(endpoint, accessKey, secretKey);
    }
}
