package com.trendvelle.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="cart_items")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id") private User user;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="product_id") private Product product;
    private String selectedSize;
    private Integer quantity = 1;
    private LocalDateTime addedAt = LocalDateTime.now();
    public BigDecimal getSubtotal() { return product.getPrice().multiply(BigDecimal.valueOf(quantity)); }
}
