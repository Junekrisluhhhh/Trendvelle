package com.trendvelle.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name="products")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String name;
    @Column(columnDefinition="TEXT") private String description;
    @Column(nullable=false) private BigDecimal price;
    private Integer stockQuantity = 0;
    private String sizes;
    private String icon;        // emoji fallback
    private String imagePath;   // actual uploaded photo path
    private boolean active = true;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;

    @OneToMany(mappedBy="product", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<ProductReview> reviews;

    @Column(updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) return 0;
        return reviews.stream().mapToInt(ProductReview::getRating).average().orElse(0);
    }
    public int getReviewCount() { return reviews == null ? 0 : reviews.size(); }

    /** Returns imagePath if available, otherwise returns icon emoji */
    public boolean hasImage() { return imagePath != null && !imagePath.isBlank(); }
}
