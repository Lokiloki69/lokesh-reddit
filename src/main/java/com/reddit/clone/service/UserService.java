package com.reddit.clone.service;

import com.reddit.clone.entity.User;
import com.reddit.clone.repository.UserRepository;
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

    private final UserRepository userRepository;

    public UserService(UserRepository repo) {
        this.userRepository = repo;
    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        String generatedUsername = generateUniqueUsername();
        user.setUsername(generatedUsername);

        user.setCreatedDate(Instant.now());
        return userRepository.save(user);
    }

    public String generateUniqueUsername() {
        String username;
        do {
            username = BASE_NAME + generateRandomString();
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

}
