package com.reddit.clone.service;

import com.reddit.clone.dto.PostDto;
import com.reddit.clone.entity.Community;
import com.reddit.clone.entity.Post;
import com.reddit.clone.exception.CommunityNotFoundException;
import com.reddit.clone.exception.PostNotFoundException;
import com.reddit.clone.mapper.PostMapper;
import com.reddit.clone.repository.CommunityRepository;
import com.reddit.clone.repository.PostRepository;
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
public class PostService {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final PostMapper postMapper;
    // private final AuthService authService; // Will be added later

    public PostDto save(PostDto postDto) {
        Community community = communityRepository.findByName(postDto.getCommunityName())
                .orElseThrow(() -> new CommunityNotFoundException("Community not found: " + postDto.getCommunityName()));

        Post post = postMapper.mapDtoToEntity(postDto);
        post.setCommunity(community);
        // post.setUser(authService.getCurrentUser()); // Will be added later

        Post savedPost = postRepository.save(post);
        log.info("Post created: {}", savedPost.getTitle());

        return postMapper.mapEntityToDto(savedPost);
    }

    @Transactional(readOnly = true)
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + id));
        return postMapper.mapEntityToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapEntityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(postMapper::mapEntityToDto);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getPostsByCommunity(Long communityId, Pageable pageable) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityNotFoundException("Community not found with ID: " + communityId));

        return postRepository.findByCommunity(community, pageable)
                .map(postMapper::mapEntityToDto);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getHotPosts(Pageable pageable) {
        return postRepository.findHotPosts(pageable)
                .map(postMapper::mapEntityToDto);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getNewPosts(Pageable pageable) {
        return postRepository.findNewPosts(pageable)
                .map(postMapper::mapEntityToDto);
    }

    public PostDto updatePost(Long id, PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + id));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageUrl(postDto.getImageUrl());
        post.setPostType(postDto.getPostType());

        Post updatedPost = postRepository.save(post);
        return postMapper.mapEntityToDto(updatedPost);
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException("Post not found with ID: " + id);
        }
        postRepository.deleteById(id);
        log.info("Post deleted with ID: {}", id);
    }
}

