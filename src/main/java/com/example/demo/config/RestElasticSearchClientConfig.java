package com.example.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 表示这是一个配置类
@Configuration
public class RestElasticSearchClientConfig {
    // 将方法的返回结果交给spring管理
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 主机ip和端口号以及协议
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                new HttpHost("172.16.2.200", 9200, "http")));
        return restHighLevelClient;
    }
}

