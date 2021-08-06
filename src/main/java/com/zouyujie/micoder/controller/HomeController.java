package com.zouyujie.micoder.controller;

import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.entity.Page;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.service.DiscussService;
import com.zouyujie.micoder.service.LikeService;
import com.zouyujie.micoder.service.UserService;
import com.zouyujie.micoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private DiscussService discussService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @RequestMapping(path = {"/index","/"},method = RequestMethod.GET)
    public String index(Model model, Page page, HttpServletRequest request){
        // 方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        User user = new User();
        user.setId(0);
        page.setRows(discussService.selectDiscussesRows(user));
        page.setPath("/index");
        List<Discuss> list = discussService.selectDiscussesByUserId(user,page.getOffset(),page.getLimit());
        List<Map<String,Object>> discusses = new ArrayList<>();
        if(list != null) {
            for (Discuss post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                long likeCount = likeService.getKeySetCount(11,1,post.getId());
                User userTemp = userService.findUserById(post.getUser().getId());
                map.put("likeCount", likeCount);
                map.put("user", userTemp);
                discusses.add(map);
            }
        }
        model.addAttribute("discussPosts",discusses);
        return "/index";
    }
}
