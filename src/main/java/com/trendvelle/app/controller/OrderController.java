package com.trendvelle.app.controller;
import com.trendvelle.app.model.Order;
import com.trendvelle.app.model.User;
import com.trendvelle.app.service.CartService;
import com.trendvelle.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller @RequestMapping("/orders") @RequiredArgsConstructor
public class OrderController extends BaseController {
    private final OrderService orderService;
    private final CartService cartService;
    @GetMapping("/checkout") public String checkout(Model model){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; var items=cartService.getCartItems(user); if(items.isEmpty()) return "redirect:/cart"; model.addAttribute("cartItems",items); model.addAttribute("cartTotal",cartService.getTotal(user)); model.addAttribute("cartCount",cartService.getCartCount(user)); model.addAttribute("user",user); return "user/checkout"; }
    @PostMapping("/place") public String placeOrder(@RequestParam String deliveryMethod,@RequestParam(required=false) String deliveryAddress,@RequestParam(required=false) String contactNumber,@RequestParam(required=false) String referenceNumber,@RequestParam(required=false) String paymentMethod,RedirectAttributes ra){
        User user=getCurrentUser(); if(user==null) return "redirect:/auth/login";
        try{Order.DeliveryMethod m=Order.DeliveryMethod.valueOf(deliveryMethod.toUpperCase()); if(m==Order.DeliveryMethod.MAXIM&&(deliveryAddress==null||deliveryAddress.isBlank()||contactNumber==null||contactNumber.isBlank()||referenceNumber==null||referenceNumber.isBlank())){ra.addFlashAttribute("error","Please fill in all delivery details.");return "redirect:/orders/checkout";} Order order=orderService.placeOrder(user,m,deliveryAddress,contactNumber,referenceNumber,paymentMethod); return "redirect:/orders/success/"+order.getOrderNumber();}
        catch(Exception e){ra.addFlashAttribute("error",e.getMessage());return "redirect:/orders/checkout";}
    }
    @GetMapping("/success/{orderNumber}") public String success(@PathVariable String orderNumber,Model model){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; return orderService.findByOrderNumber(orderNumber).map(o->{model.addAttribute("order",o);return "user/order-success";}).orElse("redirect:/orders"); }
    @GetMapping public String myOrders(Model model){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; model.addAttribute("orders",orderService.getUserOrders(user)); model.addAttribute("cartCount",cartService.getCartCount(user)); return "user/orders"; }
    @GetMapping("/{id}") public String orderDetail(@PathVariable Long id,Model model){ User user=getCurrentUser(); if(user==null) return "redirect:/auth/login"; return orderService.findById(id).filter(o->o.getUser().getId().equals(user.getId())).map(o->{model.addAttribute("order",o);model.addAttribute("cartCount",cartService.getCartCount(user));return "user/order-detail";}).orElse("redirect:/orders"); }
}
