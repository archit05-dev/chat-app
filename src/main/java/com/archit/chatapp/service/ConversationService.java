package com.archit.chatapp.service;

import com.archit.chatapp.dto.ConversationResponse;
import com.archit.chatapp.dto.CreateConversationRequest;
import com.archit.chatapp.dto.UserResponse;
import com.archit.chatapp.entity.Conversation;
import com.archit.chatapp.entity.ConversationParticipant;
import com.archit.chatapp.entity.User;
import com.archit.chatapp.repository.ConversationParticipantRepository;
import com.archit.chatapp.repository.ConversationRepository;
import com.archit.chatapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public ConversationService(
            ConversationRepository conversationRepository,
            ConversationParticipantRepository participantRepository,
            UserRepository userRepository) {

        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ConversationResponse createConversation(
            String currentUserEmail,
            CreateConversationRequest request) {

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Current user not found"));

        User otherUser = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Target user not found"));

        if (currentUser.getId().equals(otherUser.getId())) {
            throw new IllegalArgumentException(
                    "You cannot start a conversation with yourself");
        }

        Optional<Conversation> existingConversation =
                conversationRepository.findConversationBetweenUsers(
                        currentUser.getId(),
                        otherUser.getId()
                );

        if (existingConversation.isPresent()) {
            return toConversationResponse(existingConversation.get());
        }

        Conversation conversation = new Conversation();
        conversation = conversationRepository.save(conversation);

        ConversationParticipant firstParticipant =
                new ConversationParticipant(conversation, currentUser);

        ConversationParticipant secondParticipant =
                new ConversationParticipant(conversation, otherUser);

        List<ConversationParticipant> participants = new ArrayList<>();
        participants.add(firstParticipant);
        participants.add(secondParticipant);

        participantRepository.saveAll(participants);

        return toConversationResponse(conversation);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getConversations(
            String currentUserEmail) {

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Current user not found"));

        List<ConversationParticipant> memberships =
                participantRepository.findByUserId(currentUser.getId());

        List<ConversationResponse> responses = new ArrayList<>();

        for (ConversationParticipant membership : memberships) {
            Conversation conversation = membership.getConversation();

            ConversationResponse response =
                    toConversationResponse(conversation);

            responses.add(response);
        }

        return responses;
    }

    private ConversationResponse toConversationResponse(
            Conversation conversation) {

        List<ConversationParticipant> participants =
                participantRepository.findByConversationId(conversation.getId());

        List<UserResponse> participantResponses = new ArrayList<>();

        for (ConversationParticipant participant : participants) {
            User user = participant.getUser();

            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );

            participantResponses.add(userResponse);
        }

        return new ConversationResponse(
                conversation.getId(),
                participantResponses,
                conversation.getCreatedAt()
        );
    }
}