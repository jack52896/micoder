package com.zouyujie.micoder.service;

import org.springframework.stereotype.Service;

@Service
public interface LikeService {
    void like(int userId,int entityType , int entityId, int entityUserId);
    long getKeySetCount(int userId , int entityType , int entityId);
    int LikeStatus(int userId , int entityType , int entityId);
    int findUserLike(int userId);
}
