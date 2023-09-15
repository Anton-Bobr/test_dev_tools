package com.example.order_service.controller;

import com.example.order_service.dao.ClientDao;
import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.OrderStatusDto;
import com.example.order_service.entity.OrderEntity;
import com.example.order_service.entity.OrderStatusEnum;
import com.example.order_service.repository.OrderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.order_service.controller.OrderController.CLIENT_URL;
import static com.example.order_service.controller.OrderController.ORDER_URL;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ClientDao clientDao;

    private static final AtomicLong UNIQUE_INTEGER = new AtomicLong(1);

    @Test
    public void getAllUserOrders() throws Exception {

        final Long clientIdForFirstAndSecondOrder = UNIQUE_INTEGER.getAndIncrement();
        final Long clientIdForThird = UNIQUE_INTEGER.getAndIncrement();

        final OrderEntity firstOrder = generateOrder(OrderStatusEnum.OPENED, clientIdForFirstAndSecondOrder);
        final OrderEntity secondOrder = generateOrder(OrderStatusEnum.DELIVERED, clientIdForFirstAndSecondOrder);
        final OrderEntity thirdOrder = generateOrder(OrderStatusEnum.OPENED, clientIdForThird);

        final String clientEmail = "client_email_" + UNIQUE_INTEGER.getAndIncrement();

        orderRepository.saveAllAndFlush(List.of(firstOrder, secondOrder, thirdOrder));

        Mockito.when(clientDao.getUserId(clientEmail)).thenReturn(clientIdForFirstAndSecondOrder);

        final String response = mockMvc
            .perform(get(ORDER_URL + CLIENT_URL)
                         .param("email", clientEmail))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andReturn().getResponse().getContentAsString();

        final List<OrderEntity> ordersList = mapper.readValue(response, new TypeReference<>(){});

        assertEquals(ordersList.size(), 2);
        assertTrue(ordersList.contains(firstOrder));
        assertTrue(ordersList.contains(secondOrder));
    }

    @Test
    public void createNewOrder() throws Exception {

        final Long clientId = UNIQUE_INTEGER.getAndIncrement();

        final Long uniquePrefix = UNIQUE_INTEGER.getAndIncrement();

        final OrderDto dto = new OrderDto();
        dto.setClientEmail(uniquePrefix + "_ani_email");
        dto.setDescription(uniquePrefix + "_ani_description");
        dto.setStatus(OrderStatusEnum.OPENED);

        Mockito.when(clientDao.getUserId(dto.getClientEmail())).thenReturn(clientId);

        mockMvc
            .perform(post(ORDER_URL)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(mapper.writeValueAsString(dto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.status", is(dto.getStatus().name())))
            .andExpect(jsonPath("$.description", is(dto.getDescription())))
            .andExpect(jsonPath("$.clientId", is(clientId), Long.class))
            .andExpect(jsonPath("$.id", is(any(Long.class)), Long.class));
    }

    @Test
    public void updateOrderStatus() throws Exception {

        final Long clientId = UNIQUE_INTEGER.getAndIncrement();

        final OrderEntity order = generateOrder(OrderStatusEnum.OPENED, clientId);
        orderRepository.saveAndFlush(order);

        final OrderStatusDto dto = new OrderStatusDto();
        dto.setStatus(OrderStatusEnum.DELIVERED);

        mockMvc
            .perform(patch(ORDER_URL + "/" + order.getId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(mapper.writeValueAsString(dto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.status", is(dto.getStatus().name())))
            .andExpect(jsonPath("$.description", is(order.getDescription())))
            .andExpect(jsonPath("$.clientId", is(order.getClientId()), Long.class))
            .andExpect(jsonPath("$.id", is(order.getId()), Long.class));
    }

    private OrderEntity generateOrder(final OrderStatusEnum status,
                                      final Long clientId) {
        final Long uniquePrefix = UNIQUE_INTEGER.getAndIncrement();

        final OrderEntity entity = new OrderEntity();
        entity.setClientId(clientId);
        entity.setDescription(uniquePrefix + "_ani_description");
        entity.setStatus(status);
        return entity;
    }
}
