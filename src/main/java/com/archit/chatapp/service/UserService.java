package com.archit.chatapp.service;

import com.archit.chatapp.dto.UserResponse;
import com.archit.chatapp.entity.User;
import com.archit.chatapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsersExcept(Long currentUserId) {
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .map(this::toUserResponse)
                .toList();
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}