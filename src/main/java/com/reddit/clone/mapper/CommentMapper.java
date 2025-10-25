package com.reddit.clone.mapper;

import com.reddit.clone.dto.CommentDto;
import com.reddit.clone.entity.Comment;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment mapDtoToEntity(CommentDto commentDto, Post post, User user, Comment parentComment) {
        return Comment.builder()
                .text(commentDto.getText())
                .post(post)
                .user(user)
                .parentComment(parentComment)
                .build();
    }
}