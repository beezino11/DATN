//package com.huce.application.service.impl;
//
//import com.huce.application.entity.CartItem;
//import com.huce.application.entity.Product;
//import com.huce.application.entity.ShoppingCart;
//import com.huce.application.entity.User;
//import com.huce.application.repository.CartItemRepository;
//import com.huce.application.repository.ShoppingCartRepository;
//import com.huce.application.service.ShoppingCartService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Component
//public class ShoppingCartServiceImpl implements ShoppingCartService {
//    @Autowired
//    private CartItemRepository itemRepository;
//
//    @Autowired
//    private ShoppingCartRepository cartRepository;
//
//    @Override
//    public ShoppingCart addItemToCart(Product product, Long quantity, User buyer) {
//        ShoppingCart cart = buyer.getShoppingCart();
//
//        if (cart == null) {
//            cart = new ShoppingCart();
//        }
//        Set<CartItem> cartItems = cart.getCartItem();
//        CartItem cartItem = findCartItem(cartItems, product.getId());
//        if (cartItems == null) {
//            cartItems = new HashSet<>();
//            if (cartItem == null) {
//                cartItem = new CartItem();
////                cartItem.setId(1L);
//                cartItem.setProduct(product);
//                cartItem.setQuantity(quantity);
//                cartItem.setTotalPrice(quantity * product.getSalePrice());
//                cartItem.setCart(cart);
//                cartItems.add(cartItem);
//                itemRepository.save(cartItem);
//            }
//        } else {
//            if (cartItem == null) {
//                cartItem = new CartItem();
//                cartItem.setProduct(product);
//                cartItem.setQuantity(quantity);
//                cartItem.setTotalPrice(quantity * product.getSalePrice());
//                cartItem.setCart(cart);
//                cartItems.add(cartItem);
//                itemRepository.save(cartItem);
//            } else {
//                cartItem.setQuantity(cartItem.getQuantity() + quantity);
//                cartItem.setTotalPrice(cartItem.getTotalPrice() + ( quantity * product.getSalePrice()));
//                itemRepository.save(cartItem);
//            }
//        }
//        cart.setCartItem(cartItems);
//
//        int totalItems = totalItems(cart.getCartItem());
//        double totalPrice = totalPrice(cart.getCartItem());
//
//        cart.setTotalPrices(totalPrice);
//        cart.setTotalItems(totalItems);
//        cart.setBuyer(buyer);
//
//        return cartRepository.save(cart);
//    }
//
//    @Override
//    public ShoppingCart updateItemInCart(Product product, Long quantity, User buyer) {
//        ShoppingCart cart = buyer.getShoppingCart();
//
//        Set<CartItem> cartItems = cart.getCartItem();
//
//        CartItem item = findCartItem(cartItems, product.getId());
//
//        item.setQuantity(quantity);
//        item.setTotalPrice(quantity * product.getPrice());
//
//        itemRepository.save(item);
//
//        int totalItems = totalItems(cartItems);
//        double totalPrice = totalPrice(cartItems);
//
//        cart.setTotalItems(totalItems);
//        cart.setTotalPrices(totalPrice);
//
//        return cartRepository.save(cart);
//    }
//
//    @Override
//    public ShoppingCart deleteItemFromCart(Product product, User buyer) {
//        ShoppingCart cart = buyer.getShoppingCart();
//
//        Set<CartItem> cartItems = cart.getCartItem();
//
//        CartItem item = findCartItem(cartItems, product.getId());
//
//        cartItems.remove(item);
//
//        itemRepository.delete(item);
//
//        double totalPrice = totalPrice(cartItems);
//        int totalItems = totalItems(cartItems);
//
//        cart.setCartItem(cartItems);
//        cart.setTotalItems(totalItems);
//        cart.setTotalPrices(totalPrice);
//
//        return cartRepository.save(cart);
//    }
//
//    private CartItem findCartItem(Set<CartItem> cartItems, String productId) {
//        if (cartItems == null) {
//            return null;
//        }
//        CartItem cartItem = null;
//
//        for (CartItem item : cartItems) {
//            if (item.getProduct().getId().equals(productId)) {
//                cartItem = item;
//            }
//        }
//        return cartItem;
//    }
//
//    private int totalItems(Set<CartItem> cartItems){
//        int totalItems = 0;
//        for(CartItem item : cartItems){
//            totalItems += item.getQuantity();
//        }
//        return totalItems;
//    }
//
//    private double totalPrice(Set<CartItem> cartItems){
//        double totalPrice = 0.0;
//
//        for(CartItem item : cartItems){
//            totalPrice += item.getTotalPrice();
//        }
//
//        return totalPrice;
//    }
//}
