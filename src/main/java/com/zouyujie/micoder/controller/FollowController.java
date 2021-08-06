package com.zouyujie.micoder.controller;

import com.zouyujie.micoder.entity.Event;
import com.zouyujie.micoder.entity.Page;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.event.EventProducer;
import com.zouyujie.micoder.service.FollowService;
import com.zouyujie.micoder.service.UserService;
import com.zouyujie.micoder.util.HostHolder;
import com.zouyujie.micoder.util.MicoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventProducer eventProducer;
    @RequestMapping(path = "/follow" , method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId){
        User user = hostHolder.getUser();
        followService.follow(entityType,entityId, user.getId());
        Event event = new Event()
                .setUserId(user.getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setData("postId",entityId)
                .setTopic("TOPIC_FOLLOWER")
                .setEntityUserId(userService.findUserById(entityId).getId());
        eventProducer.send(event);
        return MicoderUtil.getJson(0,"已经关注了");
    }
    @RequestMapping(path = "/unfollow" , method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId){
        User user = hostHolder.getUser();
        followService.unfollow(entityType,entityId, user.getId());
        return MicoderUtil.getJson(0,"取消关注");
    }
    @RequestMapping(path = "/attention/{userId}", method = RequestMethod.GET)
    public String attention(Model model , @PathVariable("userId") int userId, Page page){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/attention/"+userId);
        page.setRows((int)followService.searchAttention(3,userId));
        List<Map<String, Object>> list = followService.getAttentions(userId, page.getOffset(), page.getLimit());
        if(list != null){
            for(Map<String, Object> map : list){
                int id = ((User) map.get("user")).getId();
                boolean hasFollower = followService.hasFollower(id,3,hostHolder.getUser().getId());
                map.put("hasFollowed",hasFollower);

            }

        }
        model.addAttribute("users",list);
        return "/site/attention";
    }
    @RequestMapping(path = "/follower/{userId}", method = RequestMethod.GET)
    public String follow(Model model, @PathVariable("userId") int userId, Page page){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/follower/"+userId);
        page.setRows((int)followService.searchFollower(3,userId));
        List<Map<String, Object>> list = followService.getFollowers(userId, page.getOffset(), page.getLimit());
        if(list != null){
            for(Map<String, Object> map : list){
                int id = ((User) map.get("user")).getId();
                boolean hasFollower = followService.hasFollower(id,3,hostHolder.getUser().getId());
                map.put("hasFollowed", hasFollower);
            }
        }
        model.addAttribute("users",list);
        return "/site/follower";
    }
}
