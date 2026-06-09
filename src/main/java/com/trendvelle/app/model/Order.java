package com.trendvelle.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name="orders")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true,nullable=false) private String orderNumber;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id")
    private User user;
    @OneToMany(mappedBy="order",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private List<OrderItem> items;
    @Column(nullable=false) private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING) private DeliveryMethod deliveryMethod = DeliveryMethod.PICKUP;
    @Enumerated(EnumType.STRING) private OrderStatus status = OrderStatus.PENDING;
    private String deliveryAddress;
    private String contactNumber;
    private String referenceNumber;
    private String paymentMethod;
    @Column(updatable=false) private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @PreUpdate public void preUpdate() { this.updatedAt = LocalDateTime.now(); }
    public enum DeliveryMethod { PICKUP, MAXIM }
    public enum OrderStatus { PENDING, PROCESSING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED }
}
