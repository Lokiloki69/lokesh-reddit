package com.reddit.clone.mapper;

import com.reddit.clone.dto.VoteDto;
import com.reddit.clone.entity.Vote;
import org.springframework.stereotype.Component;

@Component
public class VoteMapper {

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
}