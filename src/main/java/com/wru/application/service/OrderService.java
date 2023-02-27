package com.wru.application.service;

import com.wru.application.model.dto.OrderDetailDTO;
import com.wru.application.model.dto.OrderInfoDTO;
import com.wru.application.model.request.CreateOrderRequest;
import com.wru.application.model.request.UpdateDetailOrder;
import com.wru.application.model.request.UpdateStatusOrderRequest;
import com.wru.application.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Page<Order> adminGetListOrders(String id, String name, String phone, String status, String product, int page);

    Order createOrder(CreateOrderRequest createOrderRequest, long userId);

    void updateDetailOrder(UpdateDetailOrder updateDetailOrder, long id, long userId);

    Order findOrderById(long id);

    void updateStatusOrder(UpdateStatusOrderRequest updateStatusOrderRequest, long orderId, long userId);

    List<OrderInfoDTO> getListOrderOfPersonByStatus(int status, long userId);

    OrderDetailDTO userGetDetailById(long id, long userId);

    void userCancelOrder(long id, long userId);

    //Đếm số lượng đơn hàng
    long getCountOrder();

}
