package com.zouyujie.micoder;

import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.mapper.DiscussMapper;
import com.zouyujie.micoder.mapper.UserMapper;
import com.zouyujie.micoder.service.DiscussService;
import com.zouyujie.micoder.service.MessageService;
import com.zouyujie.micoder.service.UserService;
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
public class MapperTests {
    @Autowired
    private DiscussMapper discussMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Test
    public void testDicuss(){
        User user = new User();
        user.setId(0);
        List<Discuss> discusses = discussMapper.selectDiscussesByUserId(user,0,10);
        System.out.println(discusses);
        for(Discuss ds : discusses){
            System.out.println(ds.getTitle()+":"+ds.getUser().getHeaderUrl());
        }

    }
    @Test
    public void getCount(){
        User u = new User();
        u.setId(0);
       List<Discuss> discusses = discussMapper.selectDiscussesByUserId(u,1,10);
       for(Discuss discuss:discusses){
           System.out.println(discuss.getId());
            User user = userMapper.selectById(13);
            System.out.println(user.getHeaderUrl());
        }
    }
    @Test
    public void asda(){
        Discuss discuss = discussMapper.selectDiscussById(10);
        System.out.println(discuss.getTitle());
    }
    @Test
    public void message(){
        System.out.println(messageService.selectMessageByConversation("13_2",0,10));

    }
}
