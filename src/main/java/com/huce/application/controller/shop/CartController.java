//package com.huce.application.controller.shop;
//
//import com.huce.application.entity.ShoppingCart;
//import com.huce.application.service.ShoppingCartService;
//import com.huce.application.entity.Product;
//import com.huce.application.entity.User;
//import com.huce.application.service.ProductService;
//import com.huce.application.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.security.Principal;
//
//@Controller
//public class CartController {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private ShoppingCartService shoppingCartService;
//
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping("/cart")
//    public String cart(Model model, Principal principal, HttpSession session){
//        if(principal == null){
//            return "redirect:/fragment/login_model";
//        }
//        String email = principal.getName();
//        User user = userService.findByEmail(email);
//        ShoppingCart shoppingCart = user.getShoppingCart();
//        shoppingCart.getCartItem();
//        if(shoppingCart == null){
//            model.addAttribute("check", "No item in your cart");
//        }else {
//            session.setAttribute("totalItems", shoppingCart.getTotalItems());
//            model.addAttribute("subTotal", shoppingCart.getTotalPrices());
//            model.addAttribute("shoppingCart", shoppingCart);
//        }
//        return "shop/cart";
//    }
//
//
//    @PostMapping("/add-to-cart")
//    public String addItemToCart(
//            @RequestParam("id") String productId,
//            @RequestParam(value = "quantity", required = false, defaultValue = "1") Long quantity,
//            Principal principal,
//            HttpServletRequest request){
//
//        if(principal == null){
//            return "redirect:/fragment/login_model";
//        }
//        Product product = productService.getProductById(productId);
//        String email = principal.getName();
//        User user = userService.findByEmail(email);
//
//        ShoppingCart cart = shoppingCartService.addItemToCart(product, quantity, user);
//        return "redirect:/cart";
//
//    }
//
//
//    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
//    public String updateCart(@RequestParam("quantity") Long quantity,
//                             @RequestParam("id") String productId,
//                             Model model,
//                             Principal principal
//    ){
//
//        if(principal == null){
//            return "redirect:/fragment/login_model";
//        }else{
//            String email = principal.getName();
//            User user = userService.findByEmail(email);
//            Product product = productService.getProductById(productId);
//            ShoppingCart cart = shoppingCartService.updateItemInCart(product, quantity, user);
//
//            model.addAttribute("shoppingCart", cart);
//            return "redirect:/shop/cart";
//        }
//
//    }
//
//
//    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
//    public String deleteItemFromCart(@RequestParam("id") String productId,
//                                     Model model,
//                                     Principal principal){
//        if(principal == null){
//            return "redirect:/fragment/login_model";
//        }else{
//            String email = principal.getName();
//            User user = userService.findByEmail(email);
//            Product product = productService.getProductById(productId);
//            ShoppingCart cart = shoppingCartService.deleteItemFromCart(product, user);
//            model.addAttribute("shoppingCart", cart);
//            return "redirect:/shop/cart";
//        }
//    }
//}
