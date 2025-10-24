//package com.reddit.clone.controller;
//
//import com.reddit.clone.dto.VoteDto;
//import com.reddit.clone.service.VoteService;
//import jakarta.validation.Valid;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/api/votes")
//@AllArgsConstructor
//@Slf4j
//public class VoteController {
//
//    private final VoteService voteService;
//
//    @PostMapping
//    public String vote(@ModelAttribute @Valid VoteDto voteDto) {
//        try {
//            voteService.vote(voteDto);
//            return "redirect:/posts/" + voteDto.getPostId();
//        } catch (Exception e) {
//            log.error("Error processing vote", e);
//            return "redirect:/posts/" + voteDto.getPostId() + "?error=" + e.getMessage();
//        }
//    }
//
//    @GetMapping("/post/{postId}")
//    @ResponseBody
//    public ResponseEntity<?> getUserVoteForPost(@PathVariable Long postId) {
//        try {
//            VoteDto vote = voteService.getUserVoteForPost(postId);
//            return ResponseEntity.ok(vote);
//        } catch (Exception e) {
//            log.error("Error fetching user vote", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to fetch vote status");
//        }
//    }
//}
package com.reddit.clone.controller;

import com.reddit.clone.dto.VoteDto;
import com.reddit.clone.service.VoteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/votes")
@AllArgsConstructor
@Slf4j
public class VoteController {

    private final VoteService voteService;

    // --- CORRECTION APPLIED HERE: Using @ModelAttribute for Thymeleaf forms ---
    @PostMapping
    public String vote(@ModelAttribute @Valid VoteDto voteDto) {
        try {
            // Process the vote
            voteService.vote(voteDto);

            // Redirect back to the post detail page
            return "redirect:/posts/" + voteDto.getPostId();
        } catch (Exception e) {
            log.error("Error processing vote", e);
            // Redirect with an error parameter (handle this in the view later)
            return "redirect:/posts/" + voteDto.getPostId() + "?error=" + e.getMessage();
        }
    }

    // Example GET method (often used for AJAX to display current vote status)
    @GetMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<?> getUserVoteForPost(@PathVariable Long postId) {
        try {
            VoteDto vote = voteService.getUserVoteForPost(postId);
            return ResponseEntity.ok(vote);
        } catch (Exception e) {
            log.error("Error fetching user vote", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch vote status");
        }
    }
}