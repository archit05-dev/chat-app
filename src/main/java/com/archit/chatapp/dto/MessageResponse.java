package com.archit.chatapp.dto;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        UserResponse sender,
        String content,
        LocalDateTime createdAt
) {
}