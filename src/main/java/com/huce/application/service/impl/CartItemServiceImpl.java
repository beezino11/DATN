//package com.vuhien.application.service.impl;
//
//import com.vuhien.application.entity.CartItem;
//import com.vuhien.application.entity.Product;
//import com.vuhien.application.entity.User;
//import com.vuhien.application.repository.CartItemRepository;
//import com.vuhien.application.repository.ProductRepository;
//import com.vuhien.application.service.CartItemServiceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class CartItemServiceImpl implements CartItemServiceService {
//
//    private Map<Long, CartItem> map = new HashMap<>();
//    @Autowired
//    private CartItemRepository cartItemRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Override
//    public List<CartItem> listCartItems(User user) {
//        return cartItemRepository.findByBuyer(user);
//    }
//
//    @Override
//    public Long addProduct(String productId, Long quantity, User user) {
//        Long addedQuantity = quantity;
//
//        Product product = productRepository.findById(productId).get();
//
//        CartItem cartItem = cartItemRepository.findByBuyerAndProduct(user, product);
//
//        if (cartItem != null){
//            addedQuantity = cartItem.getQuantity() + quantity;
//            cartItem.setQuantity(addedQuantity);
//        }else {
//            cartItem = new CartItem();
//            cartItem.setQuantity(quantity);
//            cartItem.setBuyer(user);
//            cartItem.setProduct(product);
//        }
//        cartItemRepository.save(cartItem);
//        return addedQuantity;
//    }
//
//    @Override
//    public void add(CartItem item) {
//        CartItem existedItem = map.put(item.getId(), item);
//        if (existedItem != null){
//            existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
//        }
//    }
//
//    @Override
//    public void update(String productId, Long quantity) {
//        CartItem item = map.get(productId);
//
//        item.setQuantity(quantity + item.getQuantity());
//
//        if (item.getQuantity() <= 0 ){
//            map.remove(productId);
//        }
//    }
//
//    @Override
//    public void remove(String productId) {
//        map.remove(productId);
//    }
//
//    @Override
//    public Collection<CartItem> getCartItems() {
//        return map.values();
//    }
//
//    @Override
//    public double getAmount() {
//        return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
//    }
//
//    @Override
//    public int getSize() {
//        if (map.isEmpty()){
//         return 0;
//        }
//        return map.values().size();
//    }
//
//    @Override
//    public void clear() {
//        map.clear();
//    }
//}
