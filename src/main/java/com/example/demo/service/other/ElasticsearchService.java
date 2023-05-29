package com.example.demo.service.other;


import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {

    @Autowired
    private RestHighLevelClient client;


}
