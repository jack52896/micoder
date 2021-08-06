package com.zouyujie.micoder.service.impl;

import com.zouyujie.micoder.entity.LoginTicket;
import com.zouyujie.micoder.mapper.LoginTickerMapper;
import com.zouyujie.micoder.service.LoginTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginTicketServiceImpl implements LoginTicketService {
    @Autowired
    private LoginTickerMapper loginTickerMapper;
    @Override
    public int insertLoginTicket(LoginTicket loginTicket) {
        return loginTickerMapper.insertLoginTicket(loginTicket);
    }

    @Override
    public LoginTicket selectByTicket(String ticket) {
        return loginTickerMapper.selectByTicket(ticket);
    }
}
