package com.example.order_service.controller;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.OrderStatusDto;
import com.example.order_service.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.order_service.service.order.OrderService;


import java.util.List;

@RestController
@RequestMapping(OrderController.ORDER_URL)
@RequiredArgsConstructor
public class OrderController {

    public static final String ORDER_URL = "/api/order";

    public static final String CLIENT_URL = "/client";

    @Autowired
    private OrderService orderService;

    @GetMapping(CLIENT_URL)
    public ResponseEntity<List<OrderEntity>> getAllClientOrders(@RequestParam final String email) {
        return ResponseEntity.ok(orderService.getAllClientOrders(email));
    }

    @PostMapping
    public ResponseEntity<OrderEntity> createNewOrder(@RequestBody final OrderDto dto) {
        return ResponseEntity.ok(orderService.createNewOrder(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderEntity> updateOrderStatus(@PathVariable Long id,
                                                         @RequestBody final OrderStatusDto dto) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, dto.getStatus()));
    }
}
