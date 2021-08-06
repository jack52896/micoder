package com.zouyujie.micoder;

import com.zouyujie.micoder.service.LikeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MicoderApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LikeService likeService;
    @Test
    public void RedisStrings(){
        String redisKey = "json";
        redisTemplate.opsForValue().set(redisKey , 1);
        System.out.println(redisTemplate);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));

    }
    @Test
    public void RedisHash(){
        String redisHashKey = "test:user";
        redisTemplate.opsForHash().put(redisHashKey,"id",1);
        redisTemplate.opsForHash().put(redisHashKey,"userName","张三");

        System.out.println(redisTemplate.opsForHash().get(redisHashKey,"id"));
        System.out.println(redisTemplate.opsForHash().get(redisHashKey,"userName"));



    }
    @Test
    public void RedisLists(){

    }
    @Test
    public void RedisSets(){
        String keys = "like:entity:1:7";
       // redisTemplate.opsForSet().add(keys,12);
        System.out.println(redisTemplate.keys("*"));

    }
    @Test
    public void like(){

    }
}
