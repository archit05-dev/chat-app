package com.archit.chatapp.controller;

import com.archit.chatapp.dto.ConversationResponse;
import com.archit.chatapp.dto.CreateConversationRequest;
import com.archit.chatapp.dto.MessageResponse;
import com.archit.chatapp.service.ConversationService;
import com.archit.chatapp.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    private final MessageService messageService;

    public ConversationController(
            ConversationService conversationService,
            MessageService messageService) {

        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<ConversationResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request,
            Authentication authentication) {

        ConversationResponse response =
                conversationService.createConversation(
                        authentication.getName(),
                        request
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getConversations(
            Authentication authentication) {

        List<ConversationResponse> conversations =
                conversationService.getConversations(
                        authentication.getName()
                );

        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessageHistory(
            @PathVariable Long conversationId,
            Authentication authentication) {

        List<MessageResponse> messages =
                messageService.getMessageHistory(
                        authentication.getName(),
                        conversationId
                );

        return ResponseEntity.ok(messages);
    }
}