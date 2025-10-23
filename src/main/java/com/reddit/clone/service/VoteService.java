package com.reddit.clone.service;

import com.reddit.clone.dto.VoteDto;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.User;
import com.reddit.clone.entity.Vote;
import com.reddit.clone.entity.VoteType;
import com.reddit.clone.exception.PostNotFoundException;
import com.reddit.clone.exception.RedditCloneException;
import com.reddit.clone.mapper.VoteMapper;
import com.reddit.clone.repository.PostRepository;
import com.reddit.clone.repository.UserRepository;
import com.reddit.clone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteMapper voteMapper;

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(
                        "Post not found with ID: " + voteDto.getPostId()));

        // For now, use default user until authentication is implemented
        User user = getOrCreateDefaultUser();

        Optional<Vote> existingVote = voteRepository.findByPostAndUser(post, user);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();

            // If user votes the same way again, remove the vote
            if (vote.getVoteType().equals(voteDto.getVoteType())) {
                voteRepository.delete(vote);
                updatePostVoteCount(post, voteDto.getVoteType(), true);
                log.info("Vote removed from post: {}", post.getTitle());
            } else {
                // Change vote type
                VoteType oldVoteType = vote.getVoteType();
                vote.setVoteType(voteDto.getVoteType());
                voteRepository.save(vote);
                updatePostVoteCountOnChange(post, oldVoteType, voteDto.getVoteType());
                log.info("Vote changed on post: {}", post.getTitle());
            }
        } else {
            // Create new vote
            Vote newVote = Vote.builder()
                    .voteType(voteDto.getVoteType())
                    .post(post)
                    .user(user)
                    .build();
            voteRepository.save(newVote);
            updatePostVoteCount(post, voteDto.getVoteType(), false);
            log.info("New vote created on post: {}", post.getTitle());
        }
    }

    @Transactional(readOnly = true)
    public VoteDto getUserVoteForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post not found with ID: " + postId));

        User user = getOrCreateDefaultUser();

        Optional<Vote> vote = voteRepository.findByPostAndUser(post, user);

        return vote.map(voteMapper::mapEntityToDto).orElse(null);
    }

    private void updatePostVoteCount(Post post, VoteType voteType, boolean isRemoval) {
        int currentCount = post.getVoteCount();

        if (voteType == VoteType.UPVOTE) {
            post.setVoteCount(isRemoval ? currentCount - 1 : currentCount + 1);
        } else {
            post.setVoteCount(isRemoval ? currentCount + 1 : currentCount - 1);
        }

        postRepository.save(post);
    }

    private void updatePostVoteCountOnChange(Post post, VoteType oldType, VoteType newType) {
        int currentCount = post.getVoteCount();

        if (oldType == VoteType.UPVOTE && newType == VoteType.DOWNVOTE) {
            post.setVoteCount(currentCount - 2);
        } else if (oldType == VoteType.DOWNVOTE && newType == VoteType.UPVOTE) {
            post.setVoteCount(currentCount + 2);
        }

        postRepository.save(post);
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
}
