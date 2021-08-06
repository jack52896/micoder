package com.zouyujie.micoder;

import com.zouyujie.micoder.entity.LoginTicket;
import com.zouyujie.micoder.mapper.LoginTickerMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MicoderApplication.class)
public class LoginTests {
    @Autowired
    private LoginTickerMapper loginTickerMapper;
    @Test
    public void Insert(){
        loginTickerMapper.updateLoginTicket(1,"ticket");
    }
    @Test
    public void demo(){
        LoginTicket loginTicket = loginTickerMapper.selectByTicket("ticket");
        System.out.println(loginTicket.getUser().getName());

    }

}
