package com.reddit.clone.mapper;

import com.reddit.clone.dto.UserDto;
import com.reddit.clone.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapEntityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .enabled(user.getEnabled())
                .postCount(user.getPosts() != null ?
                        user.getPosts().size() : 0)
                .commentCount(user.getComments() != null ?
                        user.getComments().size() : 0)
                .communityCount(user.getCreatedCommunities() != null ?
                        user.getCreatedCommunities().size() : 0)
                .build();
    }

    public User mapDtoToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .createdDate(userDto.getCreatedDate())
                .enabled(userDto.getEnabled())
                .build();
    }
}