package com.zouyujie.micoder.service.impl;

import com.zouyujie.micoder.service.LikeService;
import com.zouyujie.micoder.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @param userId
     * @param entityType
     * @param entityId
     */
    @Override
    public void like(int userId, int entityType, int entityId,int entityUserId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations Operations) throws DataAccessException {
                String entityLikeKey = RedisUtil.getPrefixEntityType(entityType,entityId);
                String userLikeKey = RedisUtil.getUserLike(entityUserId);
                boolean ismember = Operations.opsForSet().isMember(entityLikeKey,userId);
                Operations.multi();
                if(ismember){
                    Operations.opsForSet().remove(entityLikeKey,userId);
                    Operations.opsForValue().decrement(userLikeKey);
                }else{
                    Operations.opsForSet().add(entityLikeKey,userId);
                    Operations.opsForValue().increment(userLikeKey);
                }
                return Operations.exec();
            }
        });
    }

    /**
     * 
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public long getKeySetCount(int userId , int entityType , int entityId){
        String LikeKeySet = RedisUtil.getPrefixEntityType(entityType,entityId);
        return redisTemplate.opsForSet().size(LikeKeySet);
    }
    /**
     * 查询某人对实体的点赞状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public int LikeStatus(int userId , int entityType , int entityId){
        String LikeKeySet = RedisUtil.getPrefixEntityType(entityType,entityId);
        return redisTemplate.opsForSet().isMember(LikeKeySet,userId) ? 1:0;
    }

    @Override
    public int findUserLike(int userId) {
        String userKey = RedisUtil.getUserLike(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userKey);
        return count == null ? 0:count.intValue();
    }
}
