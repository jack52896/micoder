package com.zouyujie.micoder.service;

import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DiscussService {
     List<Discuss> selectDiscussesByUserId(User user,int offset,int limit);
     int selectDiscussesRows(User user);
     int insert(Discuss discuss);
     Discuss selectDiscussById(int id);
     List<Discuss> select();
     List<Discuss> selctDiscussByIds(List<Integer> ids);
}
