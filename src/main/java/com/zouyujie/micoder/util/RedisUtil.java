package com.zouyujie.micoder.util;

public class RedisUtil {
    private static final String SPILT = ":";
    private static final String PREFIX_ENTITY_TYPE="like:entity";
    private static final String PREFIX_USER_LIKE="like:user";
    private static final String FOLLOWER = "follower";
    private static final String ATTENTION = "attention";

    public static String getPrefixEntityType(int entityType , int entityId){
        return PREFIX_ENTITY_TYPE+SPILT+entityType+SPILT+entityId;
    }
    public static String getUserLike(int userId){
        return PREFIX_USER_LIKE+SPILT+userId;
    }
    //key = follower:3:1, value = userId
    public static String follower(int entityType,int entityId){
        return FOLLOWER+SPILT+entityType+SPILT+entityId;
    }
    //key = attention:3:1 , value = entityId
    // 关注的实体类的id
    public static String attention(int entityType, int userId){
        return ATTENTION+SPILT+entityType+SPILT+userId;
    }
}
