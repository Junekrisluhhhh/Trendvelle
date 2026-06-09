package com.trendvelle.app.service;
import com.trendvelle.app.model.ChatMessage;
import com.trendvelle.app.model.User;
import com.trendvelle.app.repository.ChatMessageRepository;
import com.trendvelle.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    public List<ChatMessage> getMessages(User user){ return chatMessageRepository.findByUserOrderBySentAtAsc(user); }
    @Transactional public ChatMessage sendUserMessage(User user,String msg){ return chatMessageRepository.save(ChatMessage.builder().user(user).message(msg).senderType(ChatMessage.SenderType.USER).read(false).build()); }
    @Transactional public ChatMessage sendStaffReply(User user,String msg){ return chatMessageRepository.save(ChatMessage.builder().user(user).message(msg).senderType(ChatMessage.SenderType.STAFF).read(true).build()); }
    @Transactional public void markAsRead(User user){ chatMessageRepository.findByUserOrderBySentAtAsc(user).forEach(m->{if(!m.isRead()){m.setRead(true);chatMessageRepository.save(m);}}); }
    public List<User> getChatUsers(){ return userRepository.findAll().stream().filter(u->!chatMessageRepository.findByUserOrderBySentAtAsc(u).isEmpty()).toList(); }
    public long countUnread(){ return chatMessageRepository.countUnreadFromUsers(); }
}
