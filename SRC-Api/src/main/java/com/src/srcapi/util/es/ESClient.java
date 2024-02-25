package com.src.srcapi.util.es;

import com.alibaba.fastjson.JSON;
import com.src.srcapi.model.dto.retrieveRecord.ESCodeDocInfoDTO;
import com.src.srcapi.model.dto.retrieveRecord.es.CommitFileMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class ESClient {
    private final RestHighLevelClient client;

    public ESClient(String host, Integer port, String protocol) {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, protocol))
        );
    }

    public List<ESCodeDocInfoDTO> retrieveCode(CommitFileMeta commitFileMeta){
        List<ESCodeDocInfoDTO> esCodeDocInfoDTOList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("flink");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if(!commitFileMeta.getTag().equals("full")) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tag", commitFileMeta.getTag());
            boolQuery = boolQuery.must(termQueryBuilder);
        }
        MatchQueryBuilder classTermQuery = QueryBuilders.matchQuery("classList",String.join(", ", commitFileMeta.getClassList()));
        MatchQueryBuilder fieldTermQuery = QueryBuilders.matchQuery("fieldList",String.join(", ", commitFileMeta.getFieldList()));
        MatchQueryBuilder methodTermQuery = QueryBuilders.matchQuery("methodList",String.join(", ", commitFileMeta.getMethodList()));
        boolQuery = boolQuery
                .must(classTermQuery)
                .must(fieldTermQuery)
                .must(methodTermQuery);
        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();
            for(SearchHit searchHit : searchHits.getHits()){
                ESCodeDocInfoDTO esCodeDocInfoDTO = new ESCodeDocInfoDTO();
                esCodeDocInfoDTO.setCodeTag(searchHit.getId().replace("_", "/"));
                esCodeDocInfoDTO.setRepoId(Long.valueOf((Integer) ((Map<String, Object>) JSON.parse(searchHit.getSourceAsString())).get("repositoryId")));
                esCodeDocInfoDTOList.add(esCodeDocInfoDTO);
                log.info(searchHit.getSourceAsString());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return esCodeDocInfoDTOList;
    }

    public static void main(String[] args) {
        ESClient esClient = new ESClient("172.19.241.178", 9200, "http");
        CommitFileMeta commitFileMeta = new CommitFileMeta();
        commitFileMeta.setTag("util");
        commitFileMeta.setClassList(Arrays.asList("ViewHolder", "ShopSpinnerAdapter"));
        commitFileMeta.setFieldList(Arrays.asList("Context:context", "List<ProductQuantity>:quantities"));
        commitFileMeta.setMethodList(Arrays.asList("ProductsRecyclerAdapter.ViewHolder/onCreateViewHolder/ViewGroup:parent,int:viewType"));
        esClient.retrieveCode(commitFileMeta);
    }
}
