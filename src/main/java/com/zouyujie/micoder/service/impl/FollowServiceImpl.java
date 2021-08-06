package com.zouyujie.micoder.service.impl;

import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.service.FollowService;
import com.zouyujie.micoder.service.UserService;
import com.zouyujie.micoder.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Override
    public void follow(int entityType, int entityId, int userId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followKey = RedisUtil.follower(entityType,entityId);
                String attentionkey = RedisUtil.attention(userId, entityType);

                operations.multi();
                operations.opsForZSet().add(followKey,userId,System.currentTimeMillis());
                operations.opsForZSet().add(attentionkey,entityId,System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    @Override
    public void unfollow(int entityType, int entityId, int userId) {
        redisTemplate.execute(new SessionCallback(){
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followKey = RedisUtil.follower(entityType,entityId);
                String attentionkey = RedisUtil.attention(userId, entityType);

                operations.multi();
                operations.opsForZSet().remove(followKey,userId);
                operations.opsForZSet().remove(attentionkey,entityId);
                return operations.exec();
            }
        });
    }
    //key = follow:3:1
    //1为当前查看的用户
    //key = attention:3:1
    //查询多少粉丝
    @Override
    public long searchFollower(int entityType, int entityId){
        String follewKey = RedisUtil.follower(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(follewKey);
    }
    //查看关注了多少人
    @Override
    public long searchAttention(int entityType, int userId){
        String AttentionKey = RedisUtil.attention(entityType, userId);
        return redisTemplate.opsForZSet().zCard(AttentionKey);
    }
    //查询是否关注
    @Override
    public boolean hasFollower(int entityId, int entityType, int userId){
        String AttentionKey = RedisUtil.attention(userId, entityType);
        return redisTemplate.opsForZSet().score(AttentionKey, entityId) != null;
    }

    @Override
    public List<Map<String, Object>> getAttentions(int userId, int offset , int limit) {
        String AttentionKey = RedisUtil.attention(3,userId);
        Set<Integer> AttentionIds = redisTemplate.opsForZSet().range(AttentionKey, offset, offset+limit-1);
        if(AttentionIds == null){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for(Integer id : AttentionIds){
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(id.intValue());
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(AttentionKey,id);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getFollowers(int userId, int offset , int limit) {
        String FollowerKey = RedisUtil.follower(3,userId);
        System.out.println(FollowerKey);
        Set<Integer> Ids = redisTemplate.opsForZSet().range(FollowerKey,offset ,offset+limit-1);
        System.out.println(Ids);
        if(Ids == null){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for(Integer id : Ids){
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(id.intValue());
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(FollowerKey,id);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }
        return list;
    }
}
