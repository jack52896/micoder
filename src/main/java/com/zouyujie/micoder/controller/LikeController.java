package com.zouyujie.micoder.controller;

import com.zouyujie.micoder.entity.Event;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.event.EventProducer;
import com.zouyujie.micoder.service.LikeService;
import com.zouyujie.micoder.util.HostHolder;
import com.zouyujie.micoder.util.MicoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String Like(int entityUserId,int entityType,int entityId){
        System.out.println(entityUserId);
        System.out.println(entityType);
        System.out.println(entityId);

        User user = hostHolder.getUser();
        //点赞
        likeService.like(user.getId(),entityType,entityId,entityUserId);
        //点赞数量
        long count = likeService.getKeySetCount(user.getId(),entityType,entityId);
        int likeStatus = likeService.LikeStatus(user.getId(),entityType,entityId);
        Map<String,Object> map = new HashMap<>();
        if(likeStatus == 1){
            Event event = new Event()
                    .setTopic("TOPIC_LIKE")
                    .setEntityId(entityId)
                    .setEntityType(entityType)
                    .setEntityUserId(entityUserId)
                    .setData("postId",entityId)
                    .setUserId(hostHolder.getUser().getId());
            eventProducer.send(event);
        }
        map.put("likeStatus",likeStatus);
        map.put("likeCount",count);
        return MicoderUtil.getJson(0,null,map);
    }
}
