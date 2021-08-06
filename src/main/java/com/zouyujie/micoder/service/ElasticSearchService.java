package com.zouyujie.micoder.service;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public interface ElasticSearchService{
    /**
     * 全局查询
     * @param query
     * @return 符合条件的帖子的id集合
     */
    List<Integer> searchAll(String query) throws IOException;
    void insertES() throws IOException;
}
