package com.zouyujie.micoder.controller;

import ch.qos.logback.classic.net.SyslogAppender;
import com.alibaba.fastjson.JSONObject;
import com.zouyujie.micoder.config.TempConfig;
import com.zouyujie.micoder.entity.Message;
import com.zouyujie.micoder.entity.Page;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.service.MessageService;
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
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Controller
@RequestMapping("/letter")
public class MessageController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private TempConfig simpleDateFormat;
    @RequestMapping(path = "/list",method = RequestMethod.GET)
    public String list(Model model, Page page){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setRows(messageService.selectCountByConversations(user));
        page.setPath("/letter/list");
        List<Message> conversationList = messageService.selectByConversation(user,page.getOffset(),page.getLimit());
        List<Map<String,Object>> conversations = new ArrayList<>();
        if(conversationList != null){
            for(Message conversation : conversationList){
                Map<String,Object> map = new HashMap<>();
                map.put("conversation",conversation);
                map.put("letterCount",messageService.selectLetterCount(conversation.getConversationId()));
                map.put("unreadCount",messageService.selectLetterUnreadCount(conversation.getConversationId()));
                User targetId = user.getId() == conversation.getFromUser().getId()? conversation.getToUser() : conversation.getFromUser();
                map.put("target",targetId);
                conversations.add(map);
            }
        }
        model.addAttribute("letterUnreadCount",messageService.selectLetterUnreadCount(null));
        model.addAttribute("conversations",conversations);
        return "/site/letter";
    }
    @RequestMapping(path = "/detail/{conversationId}",method = RequestMethod.GET)
    public String detail(@PathVariable("conversationId") String conversationId,Page page,Model model){
        page.setLimit(5);
        page.setPath("/letter/detail/"+conversationId);
        page.setRows(messageService.selectLetterCount(conversationId));
        List<Message>  letterList =  messageService.selectMessageByConversation(conversationId,page.getOffset(),page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if(letterList != null) {
            for (Message letter : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", letter);
                map.put("fromUser", letter.getFromUser());
                letters.add(map);
            }
        }
            model.addAttribute("letters",letters);
            //获取私信目标
            model.addAttribute("target",getLetterTarget(conversationId));
            //设置已读
        if(hostHolder.getUser().getId() == ((Message)letterList.get(0)).getToUser().getId()) {
            for (Message updateMessage : letterList) {
                messageService.updateMessageStatus(updateMessage.getConversationId(), 1);
            }
        }
        return "/site/letter-detail";
    }
    public User getLetterTarget(String conversationId){
        String id[] = conversationId.split("_");
        int id0 = Integer.parseInt(id[0]);
        int id1 = Integer.parseInt(id[1]);
        if(hostHolder.getUser().getId()==id0){
            return userService.findUserById(id1);
        }
        return userService.findUserById(id0);
    }

    @RequestMapping(path = "/send",method = RequestMethod.POST)
    @ResponseBody
    public String send( String toName, String content){
        String conversationId = null;
        User target = userService.findUserByName(toName);
        if(target == null){
            return MicoderUtil.getJson(1,"目标用户不存在");
        }
        User fromUser = hostHolder.getUser();
        User toUser = userService.findUserByName(toName);
        Message message = new Message();
        message.setContent(content);
        message.setCreateTime(simpleDateFormat.simpleDateFormat().format(new Date()));
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setStatus(0);
        if(fromUser.getId()> toUser.getId()){
             conversationId = fromUser.getId()+"_"+ toUser.getId();
        }else {
            conversationId = toUser.getId() + "_" + fromUser.getId();
        }
        message.setConversationId(conversationId);
        messageService.insertMessage(message);
        return MicoderUtil.getJson(0,"发送成功");
    }
    @RequestMapping(path = "/notice", method = RequestMethod.GET)
    public String notice(Model model){
        //拿取评论的帖子
        Message messageComment = messageService.findLastMessage(hostHolder.getUser().getId(), "TOPIC_COMMENT");
        Map<String, Object> TopicCommentMap = new HashMap<>();
        if(messageComment != null){
            TopicCommentMap.put("message", messageComment);
            String content = HtmlUtils.htmlUnescape(messageComment.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
            TopicCommentMap.put("user", userService.findUserById((Integer) data.get("userId")));
            TopicCommentMap.put("entityType", data.get("entityType"));
            TopicCommentMap.put("entityId", data.get("entityId"));
            TopicCommentMap.put("postId", data.get("postId"));
            int unReadCount = messageService.selectUnReadNoticeCount(hostHolder.getUser().getId(), "TOPIC_COMMENT");
            int count = messageService.selectNoticeCount(hostHolder.getUser().getId(), "TOPIC_COMMENT");
            TopicCommentMap.put("count", count);
            TopicCommentMap.put("unread", unReadCount);
        }
        model.addAttribute("commentNotice", TopicCommentMap);
        //点赞的通知
        Message messageLike = messageService.findLastMessage(hostHolder.getUser().getId(), "TOPIC_LIKE");
        if(messageLike == null){
            return "/errorPage";
        }
        Map<String, Object> TopicLikeMap = new HashMap<>();
        if(messageLike != null){
            TopicLikeMap.put("message", messageLike);
            String content = HtmlUtils.htmlUnescape(messageLike.getContent());
            Map<String, Object> dataLike = JSONObject.parseObject(content, HashMap.class);
            TopicLikeMap.put("user", userService.findUserById((Integer) dataLike.get("userId")));
            TopicLikeMap.put("entityType", dataLike.get("entityType"));
            TopicLikeMap.put("entityId", dataLike.get("entityId"));
            TopicLikeMap.put("postId", dataLike.get("postId"));
            int unReadCount = messageService.selectUnReadNoticeCount(hostHolder.getUser().getId(), "TOPIC_LIKE");
            int count = messageService.selectNoticeCount(hostHolder.getUser().getId(), "TOPIC_LIKE");
            TopicLikeMap.put("count", count);
            TopicLikeMap.put("unread", unReadCount);
        }
        model.addAttribute("likeNotice", TopicLikeMap);
        //关注类的通知
        Message messageFollower = messageService.findLastMessage(hostHolder.getUser().getId(), "TOPIC_FOLLOWER");
        Map<String, Object> TopicFollowerMap = new HashMap<>();
        if(messageFollower == null){
            return "/errorPage";
        }
        if(messageFollower != null){
            TopicFollowerMap.put("message", messageFollower);
            String content = HtmlUtils.htmlUnescape(messageFollower.getContent());
            Map<String, Object> dataFollower = JSONObject.parseObject(content, HashMap.class);
            TopicFollowerMap.put("user", userService.findUserById((Integer) dataFollower.get("userId")));
            TopicFollowerMap.put("entityType", dataFollower.get("entityType"));
            TopicFollowerMap.put("entityId", dataFollower.get("entityId"));
            TopicFollowerMap.put("postId", dataFollower.get("postId"));
            int unReadCount = messageService.selectUnReadNoticeCount(hostHolder.getUser().getId(), "TOPIC_FOLLOWER");
            int count = messageService.selectNoticeCount(hostHolder.getUser().getId(), "TOPIC_FOLLOWER");
            TopicFollowerMap.put("count", count);
            TopicFollowerMap.put("unread", unReadCount);
        }
        model.addAttribute("followNotice", TopicFollowerMap);
        return "/site/notice";
    }
    @RequestMapping(path = "/notice/detail/{topic}", method = RequestMethod.GET)
    public String noticeDetail(Model model, @PathVariable("topic") String topic, Page page){
        page.setLimit(5);
        page.setPath("/letter/notice/detail/" + topic);
        page.setRows(messageService.selectNoticeCount(hostHolder.getUser().getId(), topic));
        List<Message> messageList = messageService.selectAllNotice(hostHolder.getUser().getId(), topic);
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if(messageList != null){
            for(Message message : messageList) {
                Map<String, Object> map = new HashMap<>();
                String content = HtmlUtils.htmlUnescape(message.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("notice", message);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                map.put("fromUser", message.getFromUser());
                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices", noticeVoList);
        //设置已经读取
        List<Integer> ids = new ArrayList<>();
        for(Message message : messageList) {
            ids.add(message.getId());
        }
        messageService.updateNoticeMessage(ids, 1);
        return "/site/notice-detail";
    }
}
