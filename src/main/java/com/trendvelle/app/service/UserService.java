package com.trendvelle.app.service;
import com.trendvelle.app.model.User;
import com.trendvelle.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public User register(String fullName,String email,String password,String phone){
        if(userRepository.existsByEmail(email)) throw new RuntimeException("Email already registered.");
        return userRepository.save(User.builder().fullName(fullName).email(email)
            .password(passwordEncoder.encode(password)).phone(phone)
            .role(User.Role.USER).status(User.AccountStatus.ACTIVE).build());
    }
    public Optional<User> findByEmail(String email){ return userRepository.findByEmail(email); }
    public Optional<User> findById(Long id){ return userRepository.findById(id); }
    public List<User> findAllUsers(){ return userRepository.findAll(); }
    @Transactional public void toggleStatus(Long id){
        userRepository.findById(id).ifPresent(u->{
            u.setStatus(u.getStatus()==User.AccountStatus.ACTIVE?User.AccountStatus.SUSPENDED:User.AccountStatus.ACTIVE);
            userRepository.save(u);
        });
    }
    @Transactional public void deleteUser(Long id){ userRepository.deleteById(id); }
    public long countUsers(){ return userRepository.countByRole(User.Role.USER); }
}
