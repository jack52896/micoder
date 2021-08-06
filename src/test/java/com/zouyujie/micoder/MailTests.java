package com.zouyujie.micoder;

import com.zouyujie.micoder.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MicoderApplication.class)
public class MailTests {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void testTextMail(){
        mailClient.sendMail("2901570623@qq.com","SpringFrameWork","你是张浩东吗");

    }
    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","study");
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);
        mailClient.sendMail("2901570623@qq.com", "HTML", content);
    }
}
