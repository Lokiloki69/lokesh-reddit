package com.reddit.clone.dto;

import com.reddit.clone.entity.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDto {

    @NotNull(message = "Vote type is required")
    private VoteType voteType;

    @NotNull(message = "Post ID is required")
    private Long postId;
}