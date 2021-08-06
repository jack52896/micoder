package com.zouyujie.micoder.service;

import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.util.MicoderConstant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public interface UserService extends MicoderConstant{
     User findUserById(int id);
     User findUserByName(String name);
     Map<String,Object> register(User user) throws UnknownHostException;
     int activation(int userId,String code);
     Map<String,Object> login(String username,String password);
     void logout(String ticket);
     int updateHeaderUrl(User user,String HeaderUrl);
     Collection<? extends GrantedAuthority> getAuthorities(int userId);
}
