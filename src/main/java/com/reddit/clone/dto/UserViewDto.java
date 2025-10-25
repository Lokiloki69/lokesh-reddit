package com.reddit.clone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserViewDto {
    // User basic info
    private Long userId;
    private String username;
    private Instant createdDate;

    // Profile info
    private String displayName;
    private String bio;
    private String profileImageUrl;
    private String bannerImageUrl;
    private String location;
    private String website;

    // Statistics
    private Integer postKarma;
    private Integer commentKarma;
    private Integer totalPosts;
    private Integer totalComments;
    private Integer totalCommunities;

    // Activity lists (for tabs)
    private List<PostDto> posts;
    private List<CommentDto> comments;
    private List<CommunityDto> communities;
    private List<PostDto> upvotedPosts;
    private List<PostDto> downvotedPosts;

}