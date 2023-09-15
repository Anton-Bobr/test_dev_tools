package com.example.order_service.service.order;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.entity.OrderEntity;
import com.example.order_service.entity.OrderStatusEnum;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.client.ClientService;
import com.example.order_service.service.client.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public List<OrderEntity> getAllClientOrders(final String email) {
        final Long clientId = clientService.getClientId(email);
        return orderRepository.findAllByClientId(clientId);
    }

    @Override
    public OrderEntity createNewOrder(final OrderDto dto) {
        final OrderEntity order = new OrderEntity();
        order.setDescription(dto.getDescription());
        order.setStatus(dto.getStatus());
        order.setClientId(clientService.getClientId(dto.getClientEmail()));

        orderRepository.saveAndFlush(order);

        return order;
    }

    @Override
    public OrderEntity updateOrderStatus(final Long id,
                                         final OrderStatusEnum status) {
        final Optional<OrderEntity> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isPresent()){
            final OrderEntity order = optionalOrder.get();
            order.setStatus(status);
            return orderRepository.saveAndFlush(order);
        } else {
            throw new RuntimeException(String.format("order with id = %s not exist", id));
        }
    }
}
