package com.archit.chatapp.dto;

public record LoginResponse(
        String token,
        Long userId,
        String name,
        String email
) {}