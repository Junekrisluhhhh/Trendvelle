package com.trendvelle.app.repository;
import com.trendvelle.app.model.Product;
import com.trendvelle.app.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {
    List<ProductReview> findByProductOrderByCreatedAtDesc(Product product);
}
