package com.trendvelle.app.config;
import com.trendvelle.app.model.User;
import com.trendvelle.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.List;
@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    @Bean public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
    @Bean public UserDetailsService userDetailsService(){
        return email->{
            User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Not found"));
            if(user.getStatus()==User.AccountStatus.SUSPENDED) throw new UsernameNotFoundException("Account suspended.");
            return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name())));
        };
    }
    @Bean public DaoAuthenticationProvider authProvider(){ var p=new DaoAuthenticationProvider(); p.setUserDetailsService(userDetailsService()); p.setPasswordEncoder(passwordEncoder()); return p; }
    @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authProvider())
            .authorizeHttpRequests(auth->auth.requestMatchers("/","/auth/**","/shop","/shop/**","/product/**","/css/**","/js/**","/images/**","/h2-console/**").permitAll().requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated())
            .formLogin(form->form.loginPage("/auth/login").loginProcessingUrl("/auth/login").usernameParameter("email").passwordParameter("password").successHandler((req,res,auth)->{boolean isAdmin=auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));res.sendRedirect(isAdmin?"/admin/dashboard":"/home");}).failureUrl("/auth/login?error=true").permitAll())
            .logout(logout->logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout","GET")).logoutSuccessUrl("/auth/login?logout=true").permitAll())
            .csrf(csrf->csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(h->h.frameOptions(f->f.disable()));
        return http.build();
    }
}
