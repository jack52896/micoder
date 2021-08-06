package com.zouyujie.micoder.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
    public static String getValue(HttpServletRequest request,String name){
        if(request==null||name==null){
            throw new IllegalArgumentException("参数错误");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equalsIgnoreCase(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
