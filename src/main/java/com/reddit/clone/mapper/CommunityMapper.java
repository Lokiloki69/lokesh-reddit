package com.reddit.clone.mapper;

import com.reddit.clone.dto.CommunityDto;
import com.reddit.clone.entity.Community;
import org.springframework.stereotype.Component;

@Component
public class CommunityMapper {

    public CommunityDto mapEntityToDto(Community community) {
        return CommunityDto.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .type(community.getType())
                .createdDate(community.getCreatedDate())
                .creatorUsername(community.getCreator() != null ?
                        community.getCreator().getUsername() : "Anonymous")
                .postCount(community.getPosts() != null ?
                        community.getPosts().size() : 0)
                .build();
    }

    public Community mapDtoToEntity(CommunityDto communityDto) {
        return Community.builder()
                .id(communityDto.getId())
                .name(communityDto.getName())
                .description(communityDto.getDescription())
                .type(communityDto.getType())
                .createdDate(communityDto.getCreatedDate())
                .build();
    }
}