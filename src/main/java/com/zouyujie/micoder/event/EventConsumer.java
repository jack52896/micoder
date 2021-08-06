package com.zouyujie.micoder.event;

import com.alibaba.fastjson.JSONObject;
import com.zouyujie.micoder.config.TempConfig;
import com.zouyujie.micoder.entity.Event;
import com.zouyujie.micoder.entity.Message;
import com.zouyujie.micoder.service.MessageService;
import com.zouyujie.micoder.service.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer {
    @Autowired
    TempConfig tempConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @KafkaListener(topics = {"TOPIC_COMMENT","TOPIC_LIKE","TOPIC_FOLLOWER"})
    public void fireMessage(ConsumerRecord consumerRecord){
        if(consumerRecord == null){
            return;
        }
        Event event = JSONObject.parseObject(consumerRecord.value().toString(), Event.class);
        if(event == null){
            return ;
        }
        //对消息进行处理
        Message message = new Message();
        message.setFromUser(userService.findUserById(1));
        message.setConversationId(event.getTopic());
        message.setToUser(userService.findUserById(event.getEntityUserId()));
        message.setCreateTime(tempConfig.simpleDateFormat().format(new Date()));

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityId", event.getEntityId());
        content.put("entityType", event.getEntityType());
        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.insertMessage(message);
    }
}
