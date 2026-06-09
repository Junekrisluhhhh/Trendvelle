package com.trendvelle.app.controller;

import com.trendvelle.app.model.Order;
import com.trendvelle.app.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Controller @RequestMapping("/admin") @RequiredArgsConstructor
public class AdminController extends BaseController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final ChatService chatService;
    private final ReviewService reviewService;

    private void common(Model model) {
        model.addAttribute("unreadChats", chatService.countUnread());
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        common(model);
        model.addAttribute("totalSales",    orderService.getTotalSales());
        model.addAttribute("todayOrders",   orderService.countTodayOrders());
        model.addAttribute("totalOrders",   orderService.countAllOrders());
        model.addAttribute("totalProducts", productService.countProducts());
        model.addAttribute("totalUsers",    userService.countUsers());
        model.addAttribute("recentOrders",  orderService.getAllOrders().stream().limit(10).toList());
        model.addAttribute("allProducts",   productService.getAllActive().stream().limit(6).toList());
        return "admin/dashboard";
    }

    // ── PRODUCTS ──
    @GetMapping("/products")
    public String products(Model model) {
        common(model);
        model.addAttribute("products",   productService.getAll());
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/products";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam BigDecimal price,
                             @RequestParam Integer stockQuantity,
                             @RequestParam(defaultValue="") String sizes,
                             @RequestParam(defaultValue="👗") String icon,
                             @RequestParam Long categoryId,
                             @RequestParam(value="imageFile", required=false) MultipartFile imageFile,
                             RedirectAttributes ra) {
        try {
            productService.saveProduct(name, description, price, stockQuantity, sizes, icon, categoryId, imageFile);
            ra.addFlashAttribute("success", "Product added successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error adding product: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam BigDecimal price,
                              @RequestParam Integer stockQuantity,
                              @RequestParam(defaultValue="") String sizes,
                              @RequestParam(defaultValue="👗") String icon,
                              @RequestParam Long categoryId,
                              @RequestParam(value="imageFile", required=false) MultipartFile imageFile,
                              RedirectAttributes ra) {
        try {
            productService.updateProduct(id, name, description, price, stockQuantity, sizes, icon, categoryId, imageFile);
            ra.addFlashAttribute("success", "Product updated!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error updating product: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        productService.deleteProduct(id);
        ra.addFlashAttribute("success", "Product removed.");
        return "redirect:/admin/products";
    }

    // ── ORDERS ──
    @GetMapping("/orders")
    public String orders(@RequestParam(required=false) String status, Model model) {
        common(model);
        var orders = orderService.getAllOrders();
        if (status != null && !status.isBlank()) {
            Order.OrderStatus os = Order.OrderStatus.valueOf(status);
            orders = orders.stream().filter(o -> o.getStatus() == os).toList();
            model.addAttribute("filterStatus", status);
        }
        model.addAttribute("orders",   orders);
        model.addAttribute("statuses", Order.OrderStatus.values());
        return "admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        common(model);
        return orderService.findById(id).map(o -> {
            model.addAttribute("order",    o);
            model.addAttribute("statuses", Order.OrderStatus.values());
            return "admin/order-detail";
        }).orElse("redirect:/admin/orders");
    }

    @PostMapping("/orders/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes ra) {
        orderService.updateStatus(id, Order.OrderStatus.valueOf(status));
        ra.addFlashAttribute("success", "Order status updated.");
        return "redirect:/admin/orders/" + id;
    }

    // ── ACCOUNTS ──
    @GetMapping("/accounts")
    public String accounts(Model model) {
        common(model);
        model.addAttribute("users", userService.findAllUsers());
        return "admin/accounts";
    }

    @PostMapping("/accounts/{id}/toggle")
    public String toggleAccount(@PathVariable Long id, RedirectAttributes ra) {
        userService.toggleStatus(id);
        ra.addFlashAttribute("success", "Account status updated.");
        return "redirect:/admin/accounts";
    }

    @PostMapping("/accounts/{id}/delete")
    public String deleteAccount(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "Account deleted.");
        return "redirect:/admin/accounts";
    }

    // ── DELIVERIES ──
    @GetMapping("/deliveries")
    public String deliveries(Model model) {
        common(model);
        model.addAttribute("maximOrders", orderService.getMaximOrders());
        model.addAttribute("statuses",    Order.OrderStatus.values());
        return "admin/deliveries";
    }

    @PostMapping("/deliveries/{id}/status")
    public String updateDelivery(@PathVariable Long id, @RequestParam String status, RedirectAttributes ra) {
        orderService.updateStatus(id, Order.OrderStatus.valueOf(status));
        ra.addFlashAttribute("success", "Delivery status updated.");
        return "redirect:/admin/deliveries";
    }

    // ── CHAT ──
    @GetMapping("/chat")
    public String chat(@RequestParam(required=false) Long userId, Model model) {
        common(model);
        model.addAttribute("chatUsers", chatService.getChatUsers());
        if (userId != null) {
            userService.findById(userId).ifPresent(u -> {
                model.addAttribute("selectedUser", u);
                model.addAttribute("messages", chatService.getMessages(u));
                chatService.markAsRead(u);
            });
        }
        return "admin/chat";
    }

    @PostMapping("/chat/reply")
    public String reply(@RequestParam Long userId, @RequestParam String message) {
        userService.findById(userId).ifPresent(u -> chatService.sendStaffReply(u, message));
        return "redirect:/admin/chat?userId=" + userId;
    }

    // ── RATINGS ──
    @GetMapping("/ratings")
    public String ratings(Model model) {
        common(model);
        var storeReviews = reviewService.getAllStoreReviews();
        if (storeReviews == null) {
            storeReviews = Collections.emptyList();
        }
        model.addAttribute("storeReviews", storeReviews);
        model.addAttribute("avgRating",    reviewService.getStoreAverageRating());
        model.addAttribute("reviewCount",  reviewService.getStoreReviewCount());
        Map<Integer, Long> ratingCounts = storeReviews.stream()
                .collect(Collectors.groupingBy(r -> r.getRating(), Collectors.counting()));
        for (int i = 1; i <= 5; i++) {
            ratingCounts.putIfAbsent(i, 0L);
        }
        model.addAttribute("ratingCounts", ratingCounts);
        return "admin/ratings";
    }
}
