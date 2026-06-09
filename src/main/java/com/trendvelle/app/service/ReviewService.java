package com.trendvelle.app.service;
import com.trendvelle.app.model.*;
import com.trendvelle.app.repository.ProductRepository;
import com.trendvelle.app.repository.ProductReviewRepository;
import com.trendvelle.app.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final StoreReviewRepository storeReviewRepository;
    private final ProductRepository productRepository;
    @Transactional public void addProductReview(User user,Long productId,int rating,String comment){ Product p=productRepository.findById(productId).orElseThrow(); productReviewRepository.save(ProductReview.builder().user(user).product(p).rating(rating).comment(comment).build()); }
    @Transactional public void addStoreReview(User user,int rating,String comment){ storeReviewRepository.save(StoreReview.builder().user(user).rating(rating).comment(comment).build()); }
    public List<ProductReview> getProductReviews(Product product){ return productReviewRepository.findByProductOrderByCreatedAtDesc(product); }
    public List<StoreReview> getAllStoreReviews(){ return storeReviewRepository.findAllByOrderByCreatedAtDesc(); }
    public double getStoreAverageRating(){ Double avg=storeReviewRepository.getAverageRating(); return avg!=null?Math.round(avg*10.0)/10.0:0; }
    public long getStoreReviewCount(){ return storeReviewRepository.count(); }
}
