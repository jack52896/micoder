package com.zouyujie.micoder.controller;

import com.zouyujie.micoder.annotation.LoginRequired;
import com.zouyujie.micoder.config.TempConfig;
import com.zouyujie.micoder.entity.Comment;
import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.entity.Page;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.service.*;
import com.zouyujie.micoder.util.CommentConstant;
import com.zouyujie.micoder.util.HostHolder;
import com.zouyujie.micoder.util.MicoderUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussController implements CommentConstant {
    @Autowired
    private TempConfig simpleDateFormat;
    @Autowired
    private DiscussService discussService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ElasticSearchService elasticSearchService;
    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(String title,String content) throws IOException {
        User user = hostHolder.getUser();
        if(user == null){
            return MicoderUtil.getJson(403,"你还没有登录哦");
        }
        if(StringUtils.isBlank(title)){
            return MicoderUtil.getJson(403,"标题不能为空哦");
        }
        if(StringUtils.isBlank(content)){
            return MicoderUtil.getJson(403,"不能发空贴子哦");
        }
        Discuss discuss = new Discuss();
        discuss.setContent(content);
        discuss.setTitle(title);
        discuss.setCreateTime(simpleDateFormat.simpleDateFormat().format(new Date()));
        discuss.setCommentCount(0);
        discuss.setStatus(0);
        discuss.setScore(1);
        discuss.setUser(user);
        discussService.insert(discuss);
        elasticSearchService.insertES();
        return MicoderUtil.getJson(0,"发布成功");
    }
    @LoginRequired
    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String detail(@PathVariable("discussPostId") int discussPostId , Model model, Page page) {
        Discuss post = discussService.selectDiscussById(+discussPostId);
        model.addAttribute("post", post);
        User user = userService.findUserById(post.getUser().getId());
        model.addAttribute("user", user);
        page.setLimit(5);
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());
        long likeCount = likeService.getKeySetCount(hostHolder.getUser().getId(),1, discussPostId);
        int likestatus = hostHolder.getUser()==null ? 0:
                likeService.LikeStatus(hostHolder.getUser().getId(),1,discussPostId);
        model.addAttribute("likeCount",likeCount);
        model.addAttribute("likeStatus",likestatus);
        List<Comment> commentList = commentService.selectCommentByEntity(
                ENTITY_TYPE_POST,post.getId(),page.getOffset(),page.getLimit());
        int commentCount = commentService.selectCount(post.getId());
        List<Map<String,Object>> commentVoList  = new ArrayList<>();
        if(commentList != null){
            for(Comment comment : commentList){
                Map<String,Object> commentVo = new HashMap<>();
                //对帖子评论的用户
                commentVo.put("comment",comment);
                commentVo.put("user",comment.getUser());
                //对评论回复的用户
                List<Comment> replyList = commentService.selectCommentByEntity(ENTITY_TYPE_COMMENT,comment.getId(),0,10);
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if(replyVoList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", userService.findUserById(reply.getUser().getId()));
                        User targetUser = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", targetUser);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys",replyVoList);
                int replyCount = commentService.selectCountByEntity(ENTITY_TYPE_POST, comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);
        model.addAttribute("commentCount",commentCount);
        return "/site/discuss-detail";

    }
    @RequestMapping(path = "/mydiscuss/{userId}", method = RequestMethod.GET)
    public String mydiscuss(Model model, @PathVariable("userId") int userId, Page page){
        User user = userService.findUserById(userId);
        page.setRows(discussService.selectDiscussesRows(user));
        page.setPath("/index");
        List<Discuss> list = discussService.selectDiscussesByUserId(user,page.getOffset(),page.getLimit());
        List<Map<String, Object>> discusses = new ArrayList<>();
        int DiscussCount = discussService.selectDiscussesRows(user);
        if(list != null){
            for(Discuss discuss : list){
                Map<String, Object> map = new HashMap<>();
                map.put("post", discuss);
                map.put("time",discuss.getCreateTime());
                long likeCount = likeService.getKeySetCount(userId,1,discuss.getId());
                map.put("likeCount",likeCount);
                discusses.add(map);
            }
        }
        model.addAttribute("discusses",DiscussCount);
        model.addAttribute("discussPosts",discusses);
        return "/site/my-post";
    }
    @PostMapping("/top")
    @ResponseBody
    public String top(){
        return MicoderUtil.getJson(0,"置顶成功");
    }
    @PostMapping("/wonderful")
    @ResponseBody
    public String wonderful(){
        return MicoderUtil.getJson(0,"加精成功");
    }
    @PostMapping("/delete")
    @ResponseBody
    public String delete(){
        return MicoderUtil.getJson(0, "删除成功");
    }

}
