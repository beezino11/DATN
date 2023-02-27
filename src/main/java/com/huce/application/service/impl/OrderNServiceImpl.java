//package com.huce.application.service.impl;
//
//import com.huce.application.entity.CartItem;
//import com.huce.application.entity.OrderDetailN;
//import com.huce.application.entity.OrderN;
//import com.huce.application.entity.ShoppingCart;
//import com.huce.application.entity.*;
//import com.huce.application.repository.OrderDetailRepository;
//import com.huce.application.service.OrderNService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.huce.application.config.Constants.ORDER_STATUS;
//
//@Component
//public class OrderNServiceImpl implements OrderNService {
//
//    @Autowired
//    private OrderDetailRepository orderDetailRepository;
//
//    @Override
//    public void saveOrder(ShoppingCart cart) {
//        OrderN orderN = new OrderN();
//        orderN.setStatus(ORDER_STATUS);
//        orderN.setBuyer(cart.getBuyer());
//        orderN.setTotalPrice(cart.getTotalPrices());
//        orderN.setStatus(ORDER_STATUS);
//        List<OrderDetailN> orderDetailNList = new ArrayList<>();
//        for (CartItem item : cart.getCartItem()){
//            OrderDetailN orderDetailN = new OrderDetailN();
//            orderDetailN.setOrderN(orderN);
//            orderDetailN.setQuantity(item.getQuantity());
//            orderDetailN.setProduct(item.getProduct());
//            orderDetailRepository.save(orderDetailN);
//            orderDetailNList.add(orderDetailN);
//        }
//    }
//}
