package com.zouyujie.micoder.mapper;

import com.zouyujie.micoder.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(User user, String headerUrl);

    int updatePassword(int userId, String password);

    List<User> select();
}
