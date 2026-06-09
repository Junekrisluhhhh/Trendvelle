package com.trendvelle.app.service;
import com.trendvelle.app.model.CartItem;
import com.trendvelle.app.model.User;
import com.trendvelle.app.repository.CartItemRepository;
import com.trendvelle.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
@Service @RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    public List<CartItem> getCartItems(User user){ return cartItemRepository.findByUser(user); }
    public long getCartCount(User user){ return cartItemRepository.countByUser(user); }
    @Transactional public void addToCart(User user,Long productId,String size,int qty){
        var product=productRepository.findById(productId).orElseThrow();
        cartItemRepository.findByUserAndProductAndSelectedSize(user,product,size).ifPresentOrElse(
            item->{item.setQuantity(item.getQuantity()+qty);cartItemRepository.save(item);},
            ()->cartItemRepository.save(CartItem.builder().user(user).product(product).selectedSize(size).quantity(qty).build())
        );
    }
    @Transactional public void removeFromCart(User user,Long itemId){ cartItemRepository.findById(itemId).ifPresent(item->{if(item.getUser().getId().equals(user.getId()))cartItemRepository.delete(item);}); }
    @Transactional public void updateQuantity(User user,Long itemId,int qty){ cartItemRepository.findById(itemId).ifPresent(item->{if(item.getUser().getId().equals(user.getId())){if(qty<=0)cartItemRepository.delete(item);else{item.setQuantity(qty);cartItemRepository.save(item);}}}); }
    @Transactional public void clearCart(User user){ cartItemRepository.deleteByUser(user); }
    public BigDecimal getTotal(User user){ return getCartItems(user).stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO,BigDecimal::add); }
}
