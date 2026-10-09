package com.archit.chatapp.controller;

import com.archit.chatapp.dto.UserResponse;
import com.archit.chatapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            Authentication authentication) {

        Long currentUserId = userService
                .getUserByEmail(authentication.getName()).id();

        return ResponseEntity.ok(
                userService.getAllUsersExcept(currentUserId)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication) {

        return ResponseEntity.ok(
                userService.getUserByEmail(authentication.getName())
        );
    }
}