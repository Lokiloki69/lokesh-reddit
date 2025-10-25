package com.reddit.clone.service;

import com.reddit.clone.dto.CommunityDto;
import com.reddit.clone.entity.Community;
import com.reddit.clone.entity.User;
import com.reddit.clone.exception.CommunityNotFoundException;
import com.reddit.clone.exception.RedditCloneException;
import com.reddit.clone.mapper.CommunityMapper;
import com.reddit.clone.repository.CommunityRepository;
import com.reddit.clone.service.auth.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final AuthService authService; // Will be added later

    public CommunityDto save(CommunityDto communityDto) {
        if (communityRepository.existsByName(communityDto.getName())) {
            throw new RedditCloneException("Community with name " + communityDto.getName() + " already exists");
        }

        Community community = communityMapper.mapDtoToEntity(communityDto);
         community.setCreator(authService.getCurrentUser()); // Will be added later

        Community savedCommunity = communityRepository.save(community);
        communityDto.setId(savedCommunity.getId());

        log.info("Community created: {}", savedCommunity.getName());
        return communityDto;
    }

    @Transactional(readOnly = true)
    public List<CommunityDto> getAllCommunities() {
        return communityRepository.findAll()
                .stream()
                .map(communityMapper::mapEntityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<CommunityDto> getAllCommunities(Pageable pageable) {
        return communityRepository.findAll(pageable)
                .map(communityMapper::mapEntityToDto);
    }

    @Transactional(readOnly = true)
    public CommunityDto getCommunityById(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityNotFoundException("Community not found with ID: " + id));
        return communityMapper.mapEntityToDto(community);
    }

    @Transactional(readOnly = true)
    public CommunityDto getCommunityByName(String name) {
        Community community = communityRepository.findByName(name)
                .orElseThrow(() -> new CommunityNotFoundException("Community not found with name: " + name));
        return communityMapper.mapEntityToDto(community);
    }

    public CommunityDto updateCommunity(Long id, CommunityDto communityDto) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityNotFoundException("Community not found with ID: " + id));

        community.setDescription(communityDto.getDescription());
        community.setType(communityDto.getType());

        Community updatedCommunity = communityRepository.save(community);
        return communityMapper.mapEntityToDto(updatedCommunity);
    }

    public void deleteCommunity(Long id) {
        if (!communityRepository.existsById(id)) {
            throw new CommunityNotFoundException("Community not found with ID: " + id);
        }
        communityRepository.deleteById(id);
        log.info("Community deleted with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<CommunityDto> searchCommunities(String keyword, Pageable pageable) {
        return communityRepository.findByKeyword(keyword, pageable)
                .map(communityMapper::mapEntityToDto);
    }
}
