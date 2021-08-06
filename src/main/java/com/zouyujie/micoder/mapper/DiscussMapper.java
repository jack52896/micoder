package com.zouyujie.micoder.mapper;

import com.zouyujie.micoder.entity.Discuss;
import com.zouyujie.micoder.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DiscussMapper {
    List<Discuss> selectDiscussesByUserId(@Param("user") User user, int offset, int limit);
    int selectDiscussesRows(@Param("user") User user);
    int insert(Discuss discuss);
    Discuss selectDiscussById(int id);
    List<Discuss> select();
    List<Discuss> selctDiscussByIds(List<Integer> ids);
}
