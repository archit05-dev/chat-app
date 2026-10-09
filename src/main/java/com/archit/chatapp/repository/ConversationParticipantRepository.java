
package com.archit.chatapp.repository;

import com.archit.chatapp.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationParticipantRepository
        extends JpaRepository<ConversationParticipant, Long> {


    List<ConversationParticipant> findByUserId(Long userId);


    boolean existsByConversationIdAndUserId(
            Long conversationId, Long userId);
}
