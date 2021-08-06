package com.zouyujie.micoder.controller;

import com.google.code.kaptcha.Producer;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.service.UserService;
import com.zouyujie.micoder.util.MicoderConstant;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.Map;

@Controller
public class LoginController implements MicoderConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Value("${micoder.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLogin(){
        return "/site/login";
    }
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String Registe(Model model, User user) throws UnknownHostException {
        Map<String,Object> map = userService.register(user);
        if(map == null|| map.isEmpty()){
            model.addAttribute("msg","我们已经给你发送了一份激活邮件");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int result = userService.activation(userId,code);
        if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg","账号重复存在");
            model.addAttribute("target","/login");
        }else if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","账号激活成功");
            model.addAttribute("target","/login");
        }else{
            model.addAttribute("msg","账号激活失败");
            model.addAttribute("target","/login");
        }
        return "/site/operate-result";
    }
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response ,HttpSession session){
        String text = kaptchaProducer.createText();
        BufferedImage bufferedImage = kaptchaProducer.createImage(text);
        session.setAttribute("kaptcha",text);
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage,"png",os);
        } catch (IOException e) {
            logger.error("验证码生成失败:"+e.getMessage());
        }
    }
    @RequestMapping(path="/login",method = RequestMethod.POST)
    public String login(Model model,String username,String password,String code,HttpSession session
            ,HttpServletResponse response) {
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !code.equalsIgnoreCase(kaptcha)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "/site/login";
        }
        Map<String,Object> map = userService.login(username,password);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(90*90);
            response.addCookie(cookie);
            return "redirect:/index";
        }else{
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket")String ticket){
            userService.logout(ticket);
            return "redirect:/login";
    }
}
