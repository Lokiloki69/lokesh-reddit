package com.reddit.clone.mapper;

import com.reddit.clone.dto.UserDto;
import com.reddit.clone.dto.UserViewDto;
import com.reddit.clone.entity.User;
import com.reddit.clone.entity.UserProfile;
import com.reddit.clone.entity.VoteType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserMapper {

    private final PostMapper postMapper;
    private final VoteMapper voteMapper;
    private final CommentMapper commentMapper;

    public UserMapper(PostMapper postMapper, VoteMapper voteMapper, CommentMapper commentMapper) {
        this.postMapper = postMapper;
        this.voteMapper = voteMapper;
        this.commentMapper = commentMapper;
    }

    public UserDto mapEntityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
//                .createdDate(user.getCreatedDate())
//                .enabled(user.getEnabled())
//                .postCount(user.getPosts() != null ?
//                        user.getPosts().size() : 0)
//                .commentCount(user.getComments() != null ?
//                        user.getComments().size() : 0)
//                .communityCount(user.getCreatedCommunities() != null ?
//                        user.getCreatedCommunities().size() : 0)
                .build();
    }

    public User mapDtoToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
//                .username(userDto.getUsername())
                .email(userDto.getEmail())
//                .createdDate(userDto.getCreatedDate())
//                .enabled(userDto.getEnabled())
                .password(userDto.getPassword() != null ?
                                userDto.getPassword() : null)
                .build();
    }

    private static final String DEFAULT_AVATAR =
            "https://www.redditstatic.com/avatars/defaults/v2/avatar_default_1.png";

    public UserViewDto mapToUserView(User user) {
        if (user == null) {
            return null;
        }

        UserProfile profile = user.getProfile();

        return UserViewDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .createdDate(user.getCreatedDate())
                .displayName(profile != null ? profile.getDisplayName() : user.getUsername())
                .bio(profile != null ? profile.getBio() : null)
                .profileImageUrl(profile != null && profile.getProfileImageUrl() != null
                        ? profile.getProfileImageUrl() : DEFAULT_AVATAR)
                .bannerImageUrl(profile != null ? profile.getBannerImageUrl() : null)
                .location(profile != null ? profile.getLocation() : null)
                .website(profile != null ? profile.getWebsite() : null)
                .postKarma(calculatePostKarma(user))
                .commentKarma(calculateCommentKarma(user))
                .totalPosts(user.getPosts() != null ? user.getPosts().size() : 0)
                .totalComments(user.getComments() != null ? user.getComments().size() : 0)
                .totalCommunities(user.getCreatedCommunities() != null ? user.getCreatedCommunities().size() : 0)
                .posts(postMapper.mapEntityToDto(user.getPosts()))
                .comments(commentMapper.mapEntityToDto(user.getComments()))
                .upvotedPosts(voteMapper.mapVoteEntityToDtoByType(user.getVotes(), VoteType.UPVOTE))
                .downvotedPosts(voteMapper.mapVoteEntityToDtoByType(user.getVotes(),VoteType.DOWNVOTE))
                .build();
    }


    private Integer calculateCommentKarma(User user) {
        if (user.getComments() == null || user.getComments().isEmpty()) {
            return 0;
        }
//        return user.getComments().stream()
//                .mapToInt(comment -> comment.getVoteCount() != null ? comment.getVoteCount() : 0)
//                .sum();
        return 0;
    }

    private Integer calculatePostKarma(User user) {
        if (user.getPosts() == null || user.getPosts().isEmpty()) {
            return 0;
        }
        return user.getPosts().stream()
                .mapToInt(post -> post.getVoteCount() != null ? post.getVoteCount() : 0)
                .sum();
    }
}
