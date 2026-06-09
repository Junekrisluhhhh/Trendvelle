package com.trendvelle.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity @Table(name="categories")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true,nullable=false) private String name;
    private String icon;
    @OneToMany(mappedBy="category",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Product> products;
}
