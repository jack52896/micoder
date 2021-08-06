package com.zouyujie.micoder.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private String topic;
    private int UserId;
    private int entityType;
    private int entityId;
    private int entityUserId;
    private Map<String, Object> data = new HashMap<>();

    public int getUserId() {
        return UserId;
    }

    public Event setUserId(int userId) {
        this.UserId = userId;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
