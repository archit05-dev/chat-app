
package com.archit.chatapp.repository;

import com.archit.chatapp.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {
}
