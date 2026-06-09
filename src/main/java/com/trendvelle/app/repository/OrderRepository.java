package com.trendvelle.app.repository;
import com.trendvelle.app.model.Order;
import com.trendvelle.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByDeliveryMethodOrderByCreatedAtDesc(Order.DeliveryMethod method);
    List<Order> findAllByOrderByCreatedAtDesc();
    @Query("SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o WHERE o.status='DELIVERED'")
    BigDecimal getTotalSales();
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :start AND o.createdAt < :end")
    long countTodayOrders(LocalDateTime start, LocalDateTime end);
}
