package com.reddit.clone.mapper;

import com.reddit.clone.dto.CommentDto;
import com.reddit.clone.entity.Comment;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public CommentDto mapEntityToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .createdDate(comment.getCreatedDate())
                .build();
    }

    public List<CommentDto> mapEntityToDto(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) return List.of();
        return comments.stream()
                .map(this::mapEntityToDto)
                .toList();
    }
}
