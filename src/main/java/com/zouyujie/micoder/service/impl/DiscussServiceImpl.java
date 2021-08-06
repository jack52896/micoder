package com.zouyujie.micoder.service.impl;

import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.mapper.DiscussMapper;
import com.zouyujie.micoder.service.DiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    private DiscussMapper discussMapper;
    @Override
    public List<Discuss> selectDiscussesByUserId(User user,int offset,int limit) {
        return discussMapper.selectDiscussesByUserId(user,offset,limit);
    }

    @Override
    public int selectDiscussesRows(User user) {
        return discussMapper.selectDiscussesRows(user);
    }

    @Override
    public int insert(Discuss discuss) {
        return discussMapper.insert(discuss);
    }

    @Override
    public Discuss selectDiscussById(int id) {
        return discussMapper.selectDiscussById(id);
    }

    @Override
    public List<Discuss> select() {
        return discussMapper.select();
    }
    @Override
    public List<Discuss> selctDiscussByIds(List<Integer> ids){
        return discussMapper.selctDiscussByIds(ids);
    }
}
