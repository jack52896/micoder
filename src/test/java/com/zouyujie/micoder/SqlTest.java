package com.zouyujie.micoder;

import com.zouyujie.micoder.entity.Comment;
import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.entity.Message;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.mapper.CommentMapper;
import com.zouyujie.micoder.mapper.DiscussMapper;
import com.zouyujie.micoder.mapper.MessageMapper;
import com.zouyujie.micoder.mapper.UserMapper;
import com.zouyujie.micoder.util.CommentConstant;
import com.zouyujie.micoder.util.MicoderUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MicoderApplication.class)
public class SqlTest implements CommentConstant {
    @Autowired
    private DiscussMapper discussMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;
    @Test
    public void sqlTest(){
       List<Comment> commentList = commentMapper.selectCommentByEntity(ENTITY_TYPE_POST,29,0,10);
       if(commentList != null) {
           for (Comment comment : commentList) {
               System.out.println("值:" + comment.getUser().getId());
           }
       }
    }
    @Test
    public void count(){
        System.out.println(userMapper.selectByName("jack"));
    }
    @Test
    public void sql(){
        System.out.println(MicoderUtil.md5("admin123"));
    }

}
