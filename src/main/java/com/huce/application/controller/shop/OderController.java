//package com.huce.application.controller.shop;
//
//import com.huce.application.entity.ShoppingCart;
//import com.huce.application.entity.User;
//import com.huce.application.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.security.Principal;
//
//@Controller
//public class OderController {
//    @Autowired
//    private UserService userService;
//
//
//    @GetMapping("/check-out")
//    public String checkout(Model model, Principal principal){
//        if(principal == null){
//            return "redirect:/login_modal";
//        }
//        String email = principal.getName();
//        User buyer = userService.findByEmail(email);
//        if(buyer.getPhone().trim().isEmpty()
//                || buyer.getFullName().trim().isEmpty()){
//
//            model.addAttribute("buyer", buyer);
//            model.addAttribute("error", "You must fill the information after checkout!");
//            return "fragment/login_modal";
//        }
////        else{
//            model.addAttribute("buyer", buyer);
//            ShoppingCart cart = buyer.getShoppingCart();
//            model.addAttribute("cart", cart);
////        }
//        return "shop/checkout";
//    }
//
//
////    @GetMapping("/order")
////    public String order(){
////        return "shop/payment";
////    }
//}
