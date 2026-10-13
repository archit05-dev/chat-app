package com.archit.chatapp.service;

import com.archit.chatapp.dto.MessageResponse;
import com.archit.chatapp.dto.UserResponse;
import com.archit.chatapp.entity.Message;
import com.archit.chatapp.entity.User;
import com.archit.chatapp.repository.ConversationParticipantRepository;
import com.archit.chatapp.repository.MessageRepository;
import com.archit.chatapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public MessageService(
            MessageRepository messageRepository,
            ConversationParticipantRepository participantRepository,
            UserRepository userRepository) {

        this.messageRepository = messageRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessageHistory(
            String currentUserEmail,
            Long conversationId) {


        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));


        boolean isParticipant =
                participantRepository
                        .existsByConversationIdAndUserId(
                                conversationId,
                                currentUser.getId()
                        );

        if (!isParticipant) {
            throw new IllegalArgumentException(
                    "You are not a participant in this conversation"
            );
        }


        List<Message> messages =
                messageRepository
                        .findByConversationIdOrderByCreatedAtAsc(
                                conversationId
                        );

        // Convert entities into safe response DTOs.
        List<MessageResponse> responses = new ArrayList<>();

        for (Message message : messages) {
            User sender = message.getSender();

            UserResponse senderResponse = new UserResponse(
                    sender.getId(),
                    sender.getName(),
                    sender.getEmail()
            );

            MessageResponse response = new MessageResponse(
                    message.getId(),
                    senderResponse,
                    message.getContent(),
                    message.getCreatedAt()
            );

            responses.add(response);
        }

        return responses;
    }
}