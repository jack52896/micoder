package com.zouyujie.micoder.service.impl;

import com.zouyujie.micoder.entity.LoginTicket;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.mapper.LoginTickerMapper;
import com.zouyujie.micoder.mapper.UserMapper;
import com.zouyujie.micoder.service.UserService;
import com.zouyujie.micoder.util.MailClient;
import com.zouyujie.micoder.util.MicoderConstant;
import com.zouyujie.micoder.util.MicoderUtil;
import com.zouyujie.micoder.util.ipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService, MicoderConstant{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginTickerMapper loginTickerMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${micoder.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findUserByName(String name) {
        return userMapper.selectByName(name);
    }

    @Override
    public Map<String, Object> register(User user) throws UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();

        Map<String,Object> map = new HashMap<>();
        if(user == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if(StringUtils.isBlank(user.getName())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("eamilMsg","邮箱不能为空");
            return map;
        }
        User u = userMapper.selectByName(user.getName());
        if(u != null){
            map.put("usernameMsg","该账号已经存在");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg","该email已经存在");
            return map;
        }
        //注册用户
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        String ip = ipUtil.getIpAddress(request);
        user.setSalt(MicoderUtil.generateUUID().substring(0, 5));
        user.setPassword(MicoderUtil.md5(user.getPassword()));
        user.setType(0);
        user.setActivationCode(MicoderUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(sdf.format(new Date()));
        user.setIp(ip);
        user.setMedia(host.getHostName());
        userMapper.insertUser(user);
        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url = domain + contextPath + "/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }

    @Override
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if(user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAIL;
        }
    }

    @Override
    public Map<String, Object> login(String username,String password) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg","账号不存在");
            return map;
        }
        /*
        账号状态为0时显示未激活状态
         */
        if(user.getStatus()==0){
            map.put("usernameMsg","账号未激活,请去激活后使用");
            return map;
        }
        String passwordTemp = MicoderUtil.md5(password);
        if(!passwordTemp.equals(user.getPassword())){
            map.put("passwordMsg","密码错误");
            return map;
        }
        /*
        生成登录凭证
         */
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setStatus(0);
        loginTicket.setUser(user);
        loginTicket.setTicket(MicoderUtil.generateUUID());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loginTicket.setCreateTime(sdf.format(new Date()));
        loginTickerMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;

    }

    @Override
    public void logout(String ticket) {
        loginTickerMapper.updateLoginTicket(1,ticket);
    }

    @Override
    public int updateHeaderUrl(User user, String HeaderUrl) {
        return userMapper.updateHeader(user,HeaderUrl);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
}
