package com.zouyujie.micoder.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zouyujie.micoder.controller.LoginController;
import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.service.DiscussService;
import com.zouyujie.micoder.service.ElasticSearchService;
import com.zouyujie.micoder.util.ElasticsearchUtil;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class ElasticSearchServiceImpl extends ElasticsearchUtil implements ElasticSearchService{
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private DiscussService discussService;
    @Override
    public List<Integer> searchAll(String query) throws IOException {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(ElasticIps).connTimeout(60000).readTimeout(60000).multiThreaded(true).build());
        jestClient = factory.getObject();
        List<Integer> ids = new ArrayList<>();
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
            Search search = new Search.Builder(searchSourceBuilder.toString())
                    .addIndex(indexName)
                    .addType(typeName)
                    .build();
            JestResult jr = jestClient.execute(search);
            String result = jr.getSourceAsString();
            String resultString = "["+result+"]";
            JSONArray jsonArray = JSONArray.parseArray(resultString);
            System.out.println(jsonArray);
            if(jsonArray.size()>0){
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    System.out.println((Integer) jsonObject.get("id"));
                    ids.add((Integer) jsonObject.get("id"));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        jestClient.close();
        return ids;
    }

    @Override
    public void insertES() throws IOException {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(ElasticIps).connTimeout(60000).readTimeout(60000).multiThreaded(true).build());
        jestClient = factory.getObject();
        List<Discuss> discussList = discussService.select();
        boolean result = false;
        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(indexName).defaultType(typeName);
        try {
            for(Discuss discuss : discussList){
                Object obj = discuss;
                Index index = new Index.Builder(obj).build();
                bulk.addAction(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BulkResult br = jestClient.execute(bulk.build());
        jestClient.close();
    }
}
