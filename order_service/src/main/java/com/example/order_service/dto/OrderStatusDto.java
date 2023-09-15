package com.example.order_service.dto;

import com.example.order_service.entity.OrderStatusEnum;
import lombok.Data;

@Data
public class OrderStatusDto {

    private OrderStatusEnum status;
}
