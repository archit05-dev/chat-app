package com.archit.chatapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ConversationResponse(
        Long id,
        List<UserResponse> participants,
        LocalDateTime createdAt
) {}