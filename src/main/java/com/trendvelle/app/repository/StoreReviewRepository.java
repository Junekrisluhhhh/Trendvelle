package com.trendvelle.app.repository;
import com.trendvelle.app.model.StoreReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface StoreReviewRepository extends JpaRepository<StoreReview,Long> {
    List<StoreReview> findAllByOrderByCreatedAtDesc();
    @Query("SELECT AVG(r.rating) FROM StoreReview r")
    Double getAverageRating();
}
