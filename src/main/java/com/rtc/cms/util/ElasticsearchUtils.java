package com.rtc.cms.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author ChenHang
 */
public class ElasticsearchUtils {
    private ElasticsearchUtils() {
    }

    private static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));

    public static RestHighLevelClient getClient() {
        return client;
    }
}
