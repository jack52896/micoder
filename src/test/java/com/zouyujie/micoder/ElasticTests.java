package com.zouyujie.micoder;

import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.service.ElasticSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MicoderApplication.class)
public class ElasticTests {
    @Autowired
    private ElasticSearchService elasticSearchService;
    @Test
    public void test() throws IOException {
        elasticSearchService.searchAll("工程师");
    }
}
