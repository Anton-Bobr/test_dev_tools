package com.example.order_service.dto;

import com.example.order_service.entity.OrderStatusEnum;
import lombok.Data;

@Data
public class OrderDto {

    private String description;
    private OrderStatusEnum status;
    private String clientEmail;
}
