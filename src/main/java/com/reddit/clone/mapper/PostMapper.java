package com.reddit.clone.mapper;

import com.reddit.clone.dto.PostDto;
import com.reddit.clone.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDto mapEntityToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .postType(post.getPostType())
                .voteCount(post.getVoteCount())
                .createdDate(post.getCreatedDate())
                .communityName(post.getCommunity() != null ?
                        post.getCommunity().getName() : null)
                .communityId(post.getCommunity() != null ?
                        post.getCommunity().getId() : null)
                .username(post.getUser() != null ?
                        post.getUser().getUsername() : "Anonymous")
                .userId(post.getUser() != null ?
                        post.getUser().getId() : null)
                .commentCount(post.getComments() != null ?
                        post.getComments().size() : 0)
                .build();
    }

    public Post mapDtoToEntity(PostDto postDto) {
        return Post.builder()
                .id(postDto.getId())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .imageUrl(postDto.getImageUrl())
                .postType(postDto.getPostType())
                .voteCount(postDto.getVoteCount() != null ?
                        postDto.getVoteCount() : 0)
                .createdDate(postDto.getCreatedDate())
                .build();
    }
}
