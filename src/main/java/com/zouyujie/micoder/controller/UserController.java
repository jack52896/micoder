package com.zouyujie.micoder.controller;

import com.zouyujie.micoder.annotation.LoginRequired;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.service.FollowService;
import com.zouyujie.micoder.service.LikeService;
import com.zouyujie.micoder.service.UserService;
import com.zouyujie.micoder.util.HostHolder;
import com.zouyujie.micoder.util.MicoderUtil;
import org.apache.catalina.Host;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${micoder.path.upload}")
    private String uploadPath;
    @Value("${micoder.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }
    @LoginRequired
    @RequestMapping(path = "/message",method = RequestMethod.GET)
    public String getMessage(){
        return "/site/letter";
    }
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeaderUrl(MultipartFile headerImage, Model model){
        if(headerImage == null){
            model.addAttribute("error","??????????????????????????????!");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        //???????????????????????????????????????
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","?????????????????????");
            return "/site/setting";
        }
        String fileNameTemp = MicoderUtil.generateUUID()+suffix;
        File file = new File(uploadPath+"/"+fileNameTemp);
        try {
            //????????????
            headerImage.transferTo(file);
        } catch (IOException e) {
            logger.error("???????????????:"+e.getMessage());
            throw new RuntimeException("??????????????????,?????????????????????");
        }
        //?????????????????????
        User user = hostHolder.getUser();
        //????????????????????????
        String url = domain+contextPath+"/user/headerUrl/"+fileNameTemp;
        userService.updateHeaderUrl(user,url);
        return "redirect:/index";
    }
    @RequestMapping(path = "/headerUrl/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response){
        //?????????????????????
        fileName = uploadPath+"/"+fileName;
        //??????response????????????
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(fileName);
            int b = 0;
            byte[] bytes = new byte[1024];
            while((b = fis.read(bytes))!= -1){
                os.write(bytes,0,b);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @LoginRequired
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfile(@PathVariable("userId") int userId, Model model){
        User user = userService.findUserById(userId);
        model.addAttribute("user",user);
        int likeCount = likeService.findUserLike(userId);
        model.addAttribute("likeCount",likeCount);
        boolean hasFollowed = false;
        //?????????????????????
        long followeCount = followService.searchFollower(3,userId);
        long attentionCount = followService.searchAttention(3,userId);
        model.addAttribute("followerCount",followeCount);
        model.addAttribute("followeeCount",attentionCount);
        if(hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollower(userId, 3, hostHolder.getUser().getId());
        }
        model.addAttribute("hasFollowed",hasFollowed);
        return "/site/profile";
    }
}