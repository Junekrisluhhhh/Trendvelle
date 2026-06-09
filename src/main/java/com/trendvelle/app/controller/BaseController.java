package com.trendvelle.app.controller;
import com.trendvelle.app.model.User;
import com.trendvelle.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public abstract class BaseController {
    @Autowired protected UserRepository userRepository;
    protected User getCurrentUser(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if(auth==null||!auth.isAuthenticated()||auth.getName().equals("anonymousUser")) return null;
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }
}
