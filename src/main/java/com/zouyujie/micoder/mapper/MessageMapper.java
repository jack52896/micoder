package com.zouyujie.micoder.mapper;

import com.zouyujie.micoder.entity.Message;
import com.zouyujie.micoder.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> selectMessageByConversation(@Param("conversationId") String conversationId, int offset, int limit);
    List<Message> selectByConversation(@Param("user")User user,int offset,int limit);
    int selectCountByConversations(@Param("user") User user);
    int selectLetterCount(String conversationId);
    int selectLetterUnreadCount(String conversationId);
    int updateMessageStatus(@Param("cid") String  cid,@Param("status") int status);
    int insertMessage(@Param("message") Message message);
    int selectUnRead(@Param("userId") int userId);
    Message selectNotice(@Param("userId") int userId,@Param("topic") String topic);
    int selectNoticeCount(@Param("userId") int userId, @Param("topic") String topic);
    int selectUnReadNoticeCount(@Param("userId") int userId, @Param("topic") String topic);
    List<Message> selectAllNotice(@Param("userId") int userId, @Param("topic") String topic);
    int updateNoticeMessage(List<Integer> ids, int status);
}
