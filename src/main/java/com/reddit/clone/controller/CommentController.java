package com.reddit.clone.controller;

import com.reddit.clone.dto.CommentDto;
import com.reddit.clone.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public String createComment(
            @PathVariable Long postId,
            @Valid @ModelAttribute CommentDto commentDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Failed to add comment: " + result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/posts/" + postId;
        }

        try {
            commentDto.setPostId(postId);
            commentService.save(commentDto);
            redirectAttributes.addFlashAttribute("success", "Comment added successfully!");
        } catch (Exception e) {
            log.error("Error creating comment", e);
            redirectAttributes.addFlashAttribute("error",
                    "Failed to add comment: " + e.getMessage());
        }

        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/comments/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete comment");
        }
    }

    // AJAX endpoint for getting comments
    @GetMapping("/api/posts/{postId}/comments")
    @ResponseBody
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
