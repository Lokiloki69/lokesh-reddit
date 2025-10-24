package com.reddit.clone.service;

import com.reddit.clone.dto.CommentDto;
import com.reddit.clone.entity.Comment;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.User;
import com.reddit.clone.exception.PostNotFoundException;
import com.reddit.clone.exception.RedditCloneException;
import com.reddit.clone.mapper.CommentMapper;
import com.reddit.clone.repository.CommentRepository;
import com.reddit.clone.repository.PostRepository;
import com.reddit.clone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public Comment save(CommentDto givenComment) {
        Post post = postRepository.findById(givenComment.getPostId())
                .orElseThrow(() -> new PostNotFoundException(
                        "Post not found with ID: " + givenComment.getPostId()));

        // For now, create a default user until authentication is implemented
        User user = getOrCreateDefaultUser();

        Comment comment = new Comment();
        comment.setText(givenComment.getText());
        comment.setPost(post);
        comment.setUser(user);

        Optional<Comment> parentComment = Optional.empty();
        if (givenComment.getParentCommentId() != null) {
            parentComment = commentRepository.findById(givenComment.getParentCommentId());
        }

        if (parentComment.isPresent()) {
            comment.setParentComment(parentComment.get());
        } else {
            comment.setParentComment(null);
        }

        Comment savedComment = commentRepository.save(comment);
        log.info("Comment created on post: {}", post.getTitle());

        if (parentComment.isPresent()) {
            List<Comment> replies = parentComment.get().getReplies();
            if (replies == null) replies = new ArrayList<>();
            replies.add(savedComment);
            parentComment.get().setReplies(replies);
        }

        return savedComment;
    }

    public List<Comment> getCommentsByParentCommentId(Long id){
        return commentRepository.findByParentCommentId(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post not found with ID: " + postId));

        return new ArrayList<>(commentRepository.findByPostOrderByCreatedDateAsc(post));
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RedditCloneException(
                        "User not found: " + username));

        return commentRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Long getCommentCountByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post not found with ID: " + postId));

        return commentRepository.countByPost(post);
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RedditCloneException("Comment not found with ID: " + id);
        }
        commentRepository.deleteById(id);
        log.info("Comment deleted with ID: {}", id);
    }

    // Helper method to create or get default user (until authentication is implemented)
    private User getOrCreateDefaultUser() {
        return userRepository.findByUsername("defaultUser")
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username("defaultUser")
                            .email("default@reddit.com")
                            .password("password")
                            .enabled(true)
                            .build();
                    return userRepository.save(newUser);
                });
    }

    @Transactional(readOnly = true)
    public List<Comment> getChildCommentsRecursive(Long id) {
        List<Comment> children = commentRepository.findByParentCommentId(id);
        for (Comment child : children) {
            List<Comment> subReplies = getChildCommentsRecursive(child.getId());
            child.setReplies(subReplies);
        }
        return children;
    }
}
