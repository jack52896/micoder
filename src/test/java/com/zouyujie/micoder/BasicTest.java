package com.zouyujie.micoder;

import com.zouyujie.micoder.config.TempConfig;
import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.entity.Message;
import com.zouyujie.micoder.mapper.DiscussMapper;
import com.zouyujie.micoder.mapper.UserMapper;
import com.zouyujie.micoder.service.MessageService;
import com.zouyujie.micoder.util.MicoderUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MicoderApplication.class)
public class BasicTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussMapper discussMapper;
    @Autowired
    private TempConfig simpleDateFormat;
    @Autowired
    private MessageService messageService;
    @Test
    public void demo(){
        System.out.println(messageService.selectUnReadNoticeCount(3,null));
    }
}
