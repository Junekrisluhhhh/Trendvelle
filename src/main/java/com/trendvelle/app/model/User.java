package com.trendvelle.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name="users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String fullName;
    @Column(unique=true,nullable=false) private String email;
    @Column(nullable=false) private String password;
    private String phone;
    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private Role role = Role.USER;
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;
    @Column(updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Order> orders;
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<ChatMessage> chatMessages;
    public enum Role { USER, ADMIN }
    public enum AccountStatus { ACTIVE, SUSPENDED }
}
