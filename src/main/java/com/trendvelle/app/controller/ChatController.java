package com.trendvelle.app.controller;
import com.trendvelle.app.model.User;
import com.trendvelle.app.service.CartService;
import com.trendvelle.app.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/chat") @RequiredArgsConstructor
public class ChatController extends BaseController {
    private final ChatService chatService;
    private final CartService cartService;
    @GetMapping public String chatPage(Model model){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; chatService.markAsRead(user); model.addAttribute("messages",chatService.getMessages(user)); model.addAttribute("cartCount",cartService.getCartCount(user)); return "user/chat"; }
    @PostMapping("/send") public String send(@RequestParam String message){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; chatService.sendUserMessage(user,message); return "redirect:/chat"; }
}
