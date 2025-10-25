package com.reddit.clone.controller;

import com.reddit.clone.dto.CommentDto;
import com.reddit.clone.entity.Comment;
import com.reddit.clone.service.CommentService;
import com.reddit.clone.util.TimeAgoUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public String createComment(
            @PathVariable Long postId,
            @RequestParam(required = false) Long parentCommentId,
            @Valid @ModelAttribute CommentDto commentDto,
            Model model) {
            commentDto.setPostId(postId);
            commentDto.setParentCommentId(parentCommentId);
            Comment savedComment = commentService.save(commentDto);
            model.addAttribute("comment", savedComment);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/deleteComment/{id}")
    public String deleteComment(@PathVariable Long id) {
            Comment comment = commentService.getCommentById(id);
            Long postId = comment.getPost().getId();
            commentService.deleteComment(id);
            return "redirect:/posts/" + postId;
        }

    @PostMapping("/editComment/{id}")
    public String updateComment(@PathVariable Long id,@ModelAttribute CommentDto commentDto) {
            Comment updatedComment = commentService.updateComment(id, commentDto);
            return "redirect:/posts/" + updatedComment.getPost().getId();
    }



    // AJAX endpoint for getting comments
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<?> getCommentsByPost(@PathVariable Long postId) {
        try {
            var comments = commentService.getCommentsByPost(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            log.error("Error fetching comments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch comments");
        }
    }
}
