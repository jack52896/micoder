package com.zouyujie.micoder.util;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * @author jack
 */
public class ElasticsearchUtil {
      public static JestClient jestClient = null;
      public static String indexName = "discuss";
      public static String typeName = "title_content";
      public static String ElasticIps = "http://127.0.0.1:9200";
}
