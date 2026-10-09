
package com.archit.chatapp.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "conversation_participants",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"conversation_id", "user_id"}
                )
        }
)
public class ConversationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ConversationParticipant() {
    }

    public ConversationParticipant(
            Conversation conversation, User user) {
        this.conversation = conversation;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public User getUser() {
        return user;
    }
}
