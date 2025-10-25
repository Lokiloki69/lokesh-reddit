package com.reddit.clone.service;

import com.reddit.clone.dto.UserViewDto;
import com.reddit.clone.entity.User;
import com.reddit.clone.exception.RedditCloneException;
import com.reddit.clone.mapper.UserMapper;
import com.reddit.clone.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class UserService {

    // We can wrap this in a separate class, for now keep it simple
    private static final String BASE_NAME = "reddit_user_";
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int RANDOM_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    public String generateUniqueUsername() {
        String username;
        int attempts = 0;
        do {
            username = BASE_NAME + generateRandomString();
            if (attempts > 10) {
                throw new RuntimeException("Failed to generate unique username");
            }
            attempts++;
        } while (userRepository.existsByUsername(username));
        return username;
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder(UserService.RANDOM_LENGTH);
        for (int i = 0; i < UserService.RANDOM_LENGTH; i++) {
            int index = random.nextInt(ALPHANUM.length());
            sb.append(ALPHANUM.charAt(index));
        }
        return sb.toString();
    }

    // Let's Keep it simple '
    //

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, UserRepository repo, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userRepository = repo;
    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String generatedUsername = generateUniqueUsername();
        user.setUsername(generatedUsername);

        user.setCreatedDate(Instant.now());
        return userRepository.save(user);
    }

    // We have to this in the mappper , Modify Later
    public UserViewDto getUserView(String username) {
        User user = userRepository.findByUsernameWithProfile(username)
                .orElseThrow(() -> new RedditCloneException("User not found: " + username));

        return userMapper.mapToUserView(user);
    }

}
