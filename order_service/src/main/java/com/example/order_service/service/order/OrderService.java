package com.example.order_service.service.order;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.entity.OrderEntity;
import com.example.order_service.entity.OrderStatusEnum;

import java.util.List;

public interface OrderService {

    List<OrderEntity> getAllClientOrders(String email);

    OrderEntity createNewOrder(OrderDto dto);

    OrderEntity updateOrderStatus(Long id, OrderStatusEnum status);
}
