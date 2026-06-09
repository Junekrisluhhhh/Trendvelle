package com.trendvelle.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="product_reviews")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductReview {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id") private Product product;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id") private User user;
    @Column(nullable=false) private Integer rating;
    @Column(columnDefinition="TEXT") private String comment;
    private LocalDateTime createdAt = LocalDateTime.now();
}
