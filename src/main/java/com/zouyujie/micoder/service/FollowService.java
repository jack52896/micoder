package com.zouyujie.micoder.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface FollowService {
    void follow(int entityType,int entityId, int userId);
    void unfollow(int entityType,int entityId, int userId);
    long searchFollower(int entityType, int entityId);
    long searchAttention(int entityType, int userId);
    boolean hasFollower(int entityId, int entityType, int userId);
    //某用户关注的人
    List<Map<String, Object>> getAttentions(int userId, int offset , int limit);
    //某用户的粉丝
    List<Map<String, Object>> getFollowers(int userId, int offset , int limit);
}
