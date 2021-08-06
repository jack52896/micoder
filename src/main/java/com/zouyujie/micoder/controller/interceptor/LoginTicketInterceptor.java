package com.zouyujie.micoder.controller.interceptor;

import com.zouyujie.micoder.entity.LoginTicket;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.service.LoginTicketService;
import com.zouyujie.micoder.service.UserService;
import com.zouyujie.micoder.util.CookieUtil;
import com.zouyujie.micoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginTicketService loginTicketService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request,"ticket");
        if(ticket != null){
            LoginTicket loginTicket = loginTicketService.selectByTicket(ticket);
            if(loginTicket != null && loginTicket.getStatus() == 0){
                User user  = userService.findUserById(loginTicket.getUser().getId());
                hostHolder.setUser(user);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null&& modelAndView != null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex
    ) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
