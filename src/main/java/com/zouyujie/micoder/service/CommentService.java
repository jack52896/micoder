package com.zouyujie.micoder.service;

import com.zouyujie.micoder.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CommentService {
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);
    int selectCountByEntity(int entityType,int entityId);
    int selectCount(int entityId);
    int addComment(Comment comment);
}
