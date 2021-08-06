package com.zouyujie.micoder.service;

import com.zouyujie.micoder.entity.LoginTicket;
import org.springframework.stereotype.Service;

@Service
public interface LoginTicketService {
    int insertLoginTicket(LoginTicket loginTicket);
    LoginTicket selectByTicket(String ticket);
}
