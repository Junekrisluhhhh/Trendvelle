package com.trendvelle.app.service;
import com.trendvelle.app.model.*;
import com.trendvelle.app.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service @RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    @Transactional public Order placeOrder(User user,Order.DeliveryMethod method,String address,String contact,String ref,String payment){
        var items=cartService.getCartItems(user);
        if(items.isEmpty()) throw new RuntimeException("Cart is empty");
        BigDecimal subtotal=cartService.getTotal(user);
        BigDecimal fee=method==Order.DeliveryMethod.MAXIM?new BigDecimal("80"):BigDecimal.ZERO;
        String num="TV-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"))+"-"+String.format("%04d",orderRepository.count()+1);
        Order order=Order.builder().orderNumber(num).user(user).totalAmount(subtotal.add(fee)).deliveryMethod(method).status(Order.OrderStatus.PENDING).deliveryAddress(address).contactNumber(contact).referenceNumber(ref).paymentMethod(payment).build();
        List<OrderItem> ois=items.stream().map(ci->OrderItem.builder().order(order).product(ci.getProduct()).selectedSize(ci.getSelectedSize()).quantity(ci.getQuantity()).unitPrice(ci.getProduct().getPrice()).build()).collect(Collectors.toList());
        order.setItems(ois);
        Order saved=orderRepository.save(order);
        cartService.clearCart(user);
        return saved;
    }
    public List<Order> getUserOrders(User user){
         return orderRepository.findByUserOrderByCreatedAtDesc(user);
         }
    public Optional<Order> findById(Long id){ return orderRepository.findById(id); }
    public Optional<Order> findByOrderNumber(String num){ return orderRepository.findByOrderNumber(num); }
    public List<Order> getAllOrders(){ return orderRepository.findAllByOrderByCreatedAtDesc(); }
    public List<Order> getMaximOrders(){ return orderRepository.findByDeliveryMethodOrderByCreatedAtDesc(Order.DeliveryMethod.MAXIM); }
    @Transactional public void updateStatus(Long id,Order.OrderStatus status){ orderRepository.findById(id).ifPresent(o->{o.setStatus(status);orderRepository.save(o);}); }
    public BigDecimal getTotalSales(){ return orderRepository.getTotalSales(); }
    public long countTodayOrders(){
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return orderRepository.countTodayOrders(start,end);
    }
    public long countAllOrders(){ return orderRepository.count(); }
}
