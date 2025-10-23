package com.reddit.clone.mapper;

import com.reddit.clone.dto.CommentDto;
import com.reddit.clone.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto mapEntityToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdDate(comment.getCreatedDate())
                .postId(comment.getPost() != null ?
                        comment.getPost().getId() : null)
                .postTitle(comment.getPost() != null ?
                        comment.getPost().getTitle() : null)
                .username(comment.getUser() != null ?
                        comment.getUser().getUsername() : "Anonymous")
                .userId(comment.getUser() != null ?
                        comment.getUser().getId() : null)
                .build();
    }

    public Comment mapDtoToEntity(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .createdDate(commentDto.getCreatedDate())
                .build();
    }
}