package com.trendvelle.app.controller;
import com.trendvelle.app.model.User;
import com.trendvelle.app.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller @RequestMapping("/cart") @RequiredArgsConstructor
public class CartController extends BaseController {
    private final CartService cartService;
    @GetMapping public String viewCart(Model model){
        User user=getCurrentUser(); if(user==null) return "redirect:/auth/login";
        model.addAttribute("cartItems",cartService.getCartItems(user));
        model.addAttribute("cartTotal",cartService.getTotal(user));
        model.addAttribute("cartCount",cartService.getCartCount(user));
        return "user/cart";
    }
    @PostMapping("/add") public String addToCart(@RequestParam Long productId,@RequestParam(defaultValue="M") String size,@RequestParam(defaultValue="1") int quantity,RedirectAttributes ra){
        User user=getCurrentUser(); if(user==null) return "redirect:/auth/login";
        cartService.addToCart(user,productId,size,quantity);
        ra.addFlashAttribute("success","Item added to cart!");
        return "redirect:/product/"+productId;
    }
    @PostMapping("/remove/{itemId}") public String removeItem(@PathVariable Long itemId){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; cartService.removeFromCart(user,itemId); return "redirect:/cart"; }
    @PostMapping("/update/{itemId}") public String updateQuantity(@PathVariable Long itemId,@RequestParam int quantity){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; cartService.updateQuantity(user,itemId,quantity); return "redirect:/cart"; }
}
