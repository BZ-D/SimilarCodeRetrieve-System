package com.src.srcapi.config;

import com.src.srcapi.util.es.ESClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {
    @Value("${es.host}")
    private String host;

    @Value("${es.port}")
    private Integer port;

    @Value("${es.protocol}")
    private String protocol;

    @Bean
    public ESClient esClient(){
        return new ESClient(host, port, protocol);
    }
}
