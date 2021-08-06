package com.zouyujie.micoder.controller;

import com.zouyujie.micoder.util.MicoderUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
public class DemoController {
    @RequestMapping(path = "/cookieSet",method = RequestMethod.GET)
    public String SetCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("name",MicoderUtil.generateUUID());
        //设置Cookie的生效范围
        cookie.setPath("/micoder/cookieSet");
        //设置cookie的生效时间
        cookie.setMaxAge(20*20);
        response.addCookie(cookie);
        return "set Cookie";
    }
    @RequestMapping(path = "/sessionSet",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("name","张三");
        session.setAttribute("age",12);
        return "set Session";
    }
    @RequestMapping(path = "/sessionGet",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("name"));
        System.out.println(session.getAttribute("age"));
        return "get Session";
    }

}
