package com.trendvelle.app.repository;
import com.trendvelle.app.model.CartItem;
import com.trendvelle.app.model.Product;
import com.trendvelle.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProductAndSelectedSize(User user,Product product,String size);
    void deleteByUser(User user);
    long countByUser(User user);
}
