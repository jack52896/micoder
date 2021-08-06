package com.zouyujie.micoder.service;

import com.zouyujie.micoder.entity.Message;
import com.zouyujie.micoder.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface MessageService {
    /**
     *
     * @param conversationId
     * @param offset
     * @param limit
     * @return List<Message>
     */
    List<Message> selectMessageByConversation( String conversationId, int offset, int limit);
    List<Message> selectByConversation(User user,int offset,int limit);
    int selectCountByConversations( User user);
    int selectLetterCount(String conversationId);
    int selectLetterUnreadCount(String conversationId);
    int updateMessageStatus(String cid,int status);
    int insertMessage(Message message);
    int selectUnRead(int userId);
    Message findLastMessage(int userId, String topic);
    int selectNoticeCount( int userId, String topic);
    int selectUnReadNoticeCount(int userId,  String topic);
    List<Message> selectAllNotice( int userId,  String topic);
    int updateNoticeMessage(List<Integer> Ids, int status);
}
