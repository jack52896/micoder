package com.zouyujie.micoder.mapper;

import com.zouyujie.micoder.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentByEntity(int entityType,int entityId,int offset,int limit);

    int selectCountByEntity(int entityType,int entityId);

    int selectCount(int entityId);

    int addComment(Comment comment);
}
