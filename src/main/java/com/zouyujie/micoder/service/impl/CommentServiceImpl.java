package com.zouyujie.micoder.service.impl;

import com.zouyujie.micoder.entity.Comment;
import com.zouyujie.micoder.mapper.CommentMapper;
import com.zouyujie.micoder.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Override
    public int selectCount(int entityId) {
        return commentMapper.selectCount(entityId);
    }

    @Override
    public int addComment(Comment comment) {
        return commentMapper.addComment(comment);
    }
}
