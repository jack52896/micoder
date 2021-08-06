package com.zouyujie.micoder.controller.interceptor;

import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.service.MessageService;
import com.zouyujie.micoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class MessageInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private MessageService messageService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            int UnreadPeopleMessage = messageService.selectUnRead(user.getId());
            int UnreadNoticeMessage = messageService.selectUnReadNoticeCount(user.getId(),null);
            modelAndView.addObject("letterUnreadCount",UnreadPeopleMessage);
            modelAndView.addObject("noticeUnreadCount",UnreadNoticeMessage);
            modelAndView.addObject("allUnreadCount",UnreadPeopleMessage+UnreadNoticeMessage);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
