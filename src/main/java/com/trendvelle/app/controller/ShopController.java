package com.trendvelle.app.controller;
import com.trendvelle.app.model.User;
import com.trendvelle.app.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequiredArgsConstructor
public class ShopController extends BaseController {
    private final ProductService productService;
    private final CartService cartService;
    private final ReviewService reviewService;
    @GetMapping("/") public String index(){ return "redirect:/home"; }
    @GetMapping("/home") public String home(Model model){
        User user=getCurrentUser();
        model.addAttribute("products",productService.getAllActive());
        model.addAttribute("categories",productService.getAllCategories());
        model.addAttribute("storeRating",reviewService.getStoreAverageRating());
        model.addAttribute("storeReviewCount",reviewService.getStoreReviewCount());
        if(user!=null) model.addAttribute("cartCount",cartService.getCartCount(user));
        return "user/home";
    }
    @GetMapping("/shop") public String shop(@RequestParam(required=false) Long category,@RequestParam(required=false) String q,Model model){
        User user=getCurrentUser();
        if(q!=null&&!q.isBlank()){model.addAttribute("products",productService.search(q));model.addAttribute("searchQuery",q);}
        else if(category!=null){model.addAttribute("products",productService.getByCategory(category));model.addAttribute("selectedCategory",category);}
        else model.addAttribute("products",productService.getAllActive());
        model.addAttribute("categories",productService.getAllCategories());
        if(user!=null) model.addAttribute("cartCount",cartService.getCartCount(user));
        return "user/shop";
    }
    @GetMapping("/product/{id}") public String productDetail(@PathVariable Long id,Model model){
        User user=getCurrentUser();
        return productService.findById(id).map(p->{
            model.addAttribute("product",p);
            model.addAttribute("reviews",reviewService.getProductReviews(p));
            model.addAttribute("sizes",p.getSizes()!=null?p.getSizes().split(","):new String[]{});
            if(user!=null) model.addAttribute("cartCount",cartService.getCartCount(user));
            return "user/product-detail";
        }).orElse("redirect:/shop");
    }
}
