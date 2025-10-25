package com.reddit.clone.mapper;

import com.reddit.clone.dto.PostDto;
import com.reddit.clone.dto.VoteDto;
import com.reddit.clone.entity.Vote;
import com.reddit.clone.entity.VoteType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VoteMapper {

    private final PostMapper postMapper;

    public VoteMapper(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public VoteDto mapEntityToDto(Vote vote) {
        return VoteDto.builder()
                .voteType(vote.getVoteType())
                .postId(vote.getPost() != null ?
                        vote.getPost().getId() : null)
                .build();
    }

    public Vote mapDtoToEntity(VoteDto voteDto) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .build();
    }

    public List<PostDto> mapVoteEntityToDtoByType(List<Vote> votes, VoteType type) {
        return votes.stream()
                .filter(vote -> vote.getVoteType() == type)
                .map(vote -> postMapper.mapEntityToDto(vote.getPost()))
                .toList();
    }

}