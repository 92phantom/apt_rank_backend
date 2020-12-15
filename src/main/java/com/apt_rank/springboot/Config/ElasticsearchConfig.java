package com.apt_rank.springboot.Config;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Elasticsearch Configuration
 *
 * @author 92phantom
 * @since 2020-12-12
 **/
@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient client(ElasticsearchProperties properties) {
        return new RestHighLevelClient(
                RestClient.builder(properties.hosts())
        );
    }
}