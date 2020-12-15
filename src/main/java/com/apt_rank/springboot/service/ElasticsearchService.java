package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.search.AptSearch;
import com.apt_rank.springboot.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ElasticsearchService {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public ElasticsearchService(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public Mono<List<AptSearch>> matchAll(String apt_name, int related_rank) {
        final Gson gson = GsonUtil.gson();

        SearchSourceBuilder searchSourceBuilder;
        searchSourceBuilder = new SearchSourceBuilder()
                .size(related_rank)
                .query(
                    QueryBuilders.matchQuery("apt_name.nori_discard", apt_name )
                );

        SearchRequest searchRequest = new SearchRequest("apt_search");
        searchRequest.source(searchSourceBuilder);

        List<AptSearch> resultList = new ArrayList<>();
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            searchResponse.getHits().forEach(item -> {
                AptSearch apt_result = gson.fromJson(item.getSourceAsString(), AptSearch.class);
                resultList.add(apt_result);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Mono.just(resultList);

    }

}