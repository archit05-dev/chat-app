package com.archit.chatapp.repository;

import com.archit.chatapp.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    @Query("""
            SELECT c
            FROM Conversation c
            JOIN ConversationParticipant cp1
                ON cp1.conversation.id = c.id
            JOIN ConversationParticipant cp2
                ON cp2.conversation.id = c.id
            WHERE cp1.user.id = :userId1
              AND cp2.user.id = :userId2
              AND (
                  SELECT COUNT(cp)
                  FROM ConversationParticipant cp
                  WHERE cp.conversation.id = c.id
              ) = 2
            """)
    Optional<Conversation> findConversationBetweenUsers(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2
    );
}