package com.zouyujie.micoder.service.impl;

import com.zouyujie.micoder.entity.Message;
import com.zouyujie.micoder.entity.User;
import com.zouyujie.micoder.mapper.MessageMapper;
import com.zouyujie.micoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> selectMessageByConversation(String conversationId, int offset, int limit) {
        return messageMapper.selectMessageByConversation(conversationId,offset,limit);
    }

    @Override
    public List<Message> selectByConversation(User user, int offset, int limit) {
        return messageMapper.selectByConversation(user,offset,limit);
    }

    @Override
    public int selectCountByConversations(User user) {
        return messageMapper.selectCountByConversations(user);
    }

    @Override
    public int selectLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    @Override
    public int selectLetterUnreadCount(String conversationId) {
        return messageMapper.selectLetterUnreadCount(conversationId);
    }

    @Override
    public int insertMessage(Message message) {
        return messageMapper.insertMessage(message);
    }

    @Override
    public int updateMessageStatus(String cid, int status) {
        return messageMapper.updateMessageStatus(cid,status);
    }

    @Override
    public int selectUnRead(int userId) {
        return messageMapper.selectUnRead(userId);
    }

    @Override
    public Message findLastMessage(int userId, String topic) {
        return messageMapper.selectNotice(userId, topic);
    }

    @Override
    public int selectNoticeCount(int userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    @Override
    public int selectUnReadNoticeCount(int userId, String topic) {
        return messageMapper.selectUnReadNoticeCount(userId, topic);
    }

    @Override
    public List<Message> selectAllNotice(int userId, String topic) {
        return messageMapper.selectAllNotice(userId, topic);
    }

    @Override
    public int updateNoticeMessage(List<Integer> Ids, int status) {
        return messageMapper.updateNoticeMessage(Ids, status);
    }
}
