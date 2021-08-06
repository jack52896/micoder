package com.zouyujie.micoder.controller;

import com.zouyujie.micoder.config.TempConfig;
import com.zouyujie.micoder.entity.Comment;
import com.zouyujie.micoder.entity.Event;
import com.zouyujie.micoder.event.EventProducer;
import com.zouyujie.micoder.service.CommentService;
import com.zouyujie.micoder.service.DiscussService;
import com.zouyujie.micoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private TempConfig tempConfig;
    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussService discussService;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/add/{PostId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("PostId")int postId, Comment comment){
        System.out.println("targetId"+comment.getTargetId());
        comment.setDateTime(tempConfig.simpleDateFormat().format(new Date()));
        comment.setStatus(0);
        if(hostHolder.getUser()==null){
            return "redirect:/site/login";
        }
        Event event = new Event()
                .setTopic("TOPIC_COMMENT")
                .setEntityType(1)
                .setUserId(hostHolder.getUser().getId())
                .setData("postId",postId)
                .setEntityUserId(discussService.selectDiscussById(postId).getUser().getId());

        eventProducer.send(event);
        comment.setUser(hostHolder.getUser());
        commentService.addComment(comment);
        return "redirect:/discuss/detail/"+postId;
    }
}
