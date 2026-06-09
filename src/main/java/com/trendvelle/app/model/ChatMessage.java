package com.trendvelle.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="chat_messages")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessage {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
    @Column(columnDefinition="TEXT",nullable=false) private String message;
    @Enumerated(EnumType.STRING) private SenderType senderType;
    private boolean read = false;
    private LocalDateTime sentAt = LocalDateTime.now();
    public enum SenderType { USER, STAFF }
}
