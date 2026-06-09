package com.trendvelle.app.controller;
import com.trendvelle.app.model.User;
import com.trendvelle.app.service.CartService;
import com.trendvelle.app.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller @RequestMapping("/reviews") @RequiredArgsConstructor
public class ReviewController extends BaseController {
    private final ReviewService reviewService;
    private final CartService cartService;
    @GetMapping("/store") public String storePage(Model model){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; model.addAttribute("cartCount",cartService.getCartCount(user)); model.addAttribute("storeReviews",reviewService.getAllStoreReviews()); model.addAttribute("avgRating",reviewService.getStoreAverageRating()); return "user/rate-store"; }
    @PostMapping("/store") public String submitStore(@RequestParam int rating,@RequestParam String comment,RedirectAttributes ra){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; reviewService.addStoreReview(user,rating,comment); ra.addFlashAttribute("success","Thank you for your review!"); return "redirect:/home"; }
    @PostMapping("/product/{productId}") public String submitProduct(@PathVariable Long productId,@RequestParam int rating,@RequestParam String comment,RedirectAttributes ra){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; reviewService.addProductReview(user,productId,rating,comment); ra.addFlashAttribute("success","Review submitted!"); return "redirect:/product/"+productId; }
}
