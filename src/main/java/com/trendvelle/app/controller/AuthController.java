package com.trendvelle.app.controller;
import com.trendvelle.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller @RequestMapping("/auth") @RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @GetMapping("/login") public String login(@RequestParam(required=false) String error,@RequestParam(required=false) String logout,Model model){
        if(error!=null) model.addAttribute("error","Invalid email or password.");
        if(logout!=null) model.addAttribute("message","Logged out successfully.");
        return "auth/login";
    }
    @GetMapping("/register") public String registerPage(){ return "auth/register"; }
    @PostMapping("/register") public String register(@RequestParam String fullName,@RequestParam String email,@RequestParam String phone,@RequestParam String password,@RequestParam String confirmPassword,RedirectAttributes ra){
        if(!password.equals(confirmPassword)){ra.addFlashAttribute("error","Passwords do not match.");return "redirect:/auth/register";}
        try{userService.register(fullName,email,password,phone);ra.addFlashAttribute("success","Account created! Please sign in.");return "redirect:/auth/login";}
        catch(RuntimeException e){ra.addFlashAttribute("error",e.getMessage());return "redirect:/auth/register";}
    }
}
