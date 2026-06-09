package com.trendvelle.app.repository;
import com.trendvelle.app.model.Category;
import com.trendvelle.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByActiveTrue();
    List<Product> findByCategoryAndActiveTrue(Category category);
    @Query("SELECT p FROM Product p WHERE p.active=true AND (LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%',:q,'%')))")
    List<Product> search(String q);
}
