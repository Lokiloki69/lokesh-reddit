package com.reddit.clone.mapper;

import com.reddit.clone.dto.PostDto;
import com.reddit.clone.dto.PostFileDto;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.PostFile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper{
    public PostDto mapEntityToDto(Post post) {

        // --- NEW LOGIC TO FIND PRIMARY FILE FOR LIST VIEW ---
        String primaryUrl = null;
        String primaryType = null;
        if(post.getFiles() != null && !post.getFiles().isEmpty()) {
            PostFile primaryFile = post.getFiles().get(0); // Take the first file
            primaryUrl = primaryFile.getFileUrl();
            primaryType = primaryFile.getFileType();
        }

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .postType(post.getPostType())
                .voteCount(post.getVoteCount())
                .createdDate(post.getCreatedDate())
                .communityName(post.getCommunity() != null ? post.getCommunity().getName() : null)
                .communityId(post.getCommunity() != null ? post.getCommunity().getId() : null)
                .username(post.getUser() != null ? post.getUser().getUsername() : "Anonymous")
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)

                // --- MAP THE NEW PRIMARY FIELDS ---
                .primaryMediaUrl(primaryUrl)
                .primaryMediaType(primaryType)
                // ------------------------------------

                .build();
    }

    public Post mapDtoToEntity(PostDto postDto) {
        return Post.builder()
                .id(postDto.getId())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .imageUrl(postDto.getImageUrl())
                .postType(postDto.getPostType())
                .voteCount(postDto.getVoteCount() != null ? postDto.getVoteCount() : 0)
                .createdDate(postDto.getCreatedDate())
                .build();
    }

    // (Mapping PostFileDto methods remain the same)
    private List<PostFileDto> mapPostFilesToDto(List<PostFile> postFiles) {
        if(postFiles == null) {
            return Collections.emptyList();
        }
        return postFiles.stream()
                .map(this::mapPostFileToDto)
                .collect(Collectors.toList());
    }

    private PostFileDto mapPostFileToDto(PostFile postFile) {
        return PostFileDto.builder()
                .fileUrl(postFile.getFileUrl())
                .fileName(postFile.getFileName())
                .fileType(postFile.getFileType())
                .build();
    }
}