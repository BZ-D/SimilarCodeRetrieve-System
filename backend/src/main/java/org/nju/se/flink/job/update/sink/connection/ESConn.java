package org.nju.se.flink.job.update.sink.connection;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.xcontent.XContentType;
import org.nju.se.flink.job.update.entity.CommitFileMeta;

import java.io.IOException;

@Slf4j
public class ESConn {

    private final RestHighLevelClient client;

    public ESConn() {
        client = new RestHighLevelClient(
          RestClient.builder(new HttpHost("172.19.241.178", 9200, "http"))
        );
    }

    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean exists(String key) {
        GetRequest getRequest = new GetRequest("flink", key);
        getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
        getRequest.storedFields("_none_");
        try {
            return client.exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public void updateFile(String key, CommitFileMeta meta) {
        if (exists(key)) {
            UpdateRequest updateRequest = new UpdateRequest("flink", key);
            updateRequest.doc(JSON.toJSONString(meta), XContentType.JSON);
            try {
                client.update(updateRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            IndexRequest request = new IndexRequest("flink");
            request.id(key);
            request.source(JSON.toJSONString(meta), XContentType.JSON);
            try {
                client.index(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void deleteFile(String key) {
        if (exists(key)) {
            DeleteRequest request = new DeleteRequest("flink", key);
            try {
                DeleteResponse dr = client.delete(request, RequestOptions.DEFAULT);
                System.out.println(dr);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
