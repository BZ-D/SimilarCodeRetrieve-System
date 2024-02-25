package com.src.srcapi.config;

import com.src.srcapi.util.ceph.CephClient;
import com.src.srcapi.util.obs.CustomOBSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OBSConfig {
    @Value("${obs.bucket-name}")
    private String bucketName;

    @Value("${obs.endpoint}")
    private String endpoint;

    @Value("${obs.ak}")
    private String ak;

    @Value("${obs.sk}")
    private String sk;
    @Bean
    public CustomOBSClient customOBSClient(){
        return new CustomOBSClient(bucketName, endpoint, ak, sk);
    }
}
