package com.zouyujie.micoder.mapper;

import com.zouyujie.micoder.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTickerMapper {
    int insertLoginTicket(LoginTicket loginTicket);
    LoginTicket selectByTicket(String ticket);
    int updateLoginTicket(int status,String ticket);
}
