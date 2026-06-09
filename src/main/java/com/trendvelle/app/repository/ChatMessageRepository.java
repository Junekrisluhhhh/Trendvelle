package com.trendvelle.app.repository;
import com.trendvelle.app.model.ChatMessage;
import com.trendvelle.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findByUserOrderBySentAtAsc(User user);
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.senderType='USER' AND m.read=false")
    long countUnreadFromUsers();
}
