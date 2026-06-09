package com.trendvelle.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="store_reviews")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class StoreReview {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id") private User user;
    @Column(nullable=false) private Integer rating;
    @Column(columnDefinition="TEXT") private String comment;
    private LocalDateTime createdAt = LocalDateTime.now();
}
