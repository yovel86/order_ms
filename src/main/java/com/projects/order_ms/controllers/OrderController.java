package com.projects.order_ms.controllers;

import com.projects.order_ms.dtos.*;
import com.projects.order_ms.dtos.ResponseStatus;
import com.projects.order_ms.models.Order;
import com.projects.order_ms.models.OrderProduct;
import com.projects.order_ms.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return this.orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public GetOrderResponseDTO getOrderById(@PathVariable("id") long orderId) {
        GetOrderResponseDTO responseDTO = new GetOrderResponseDTO();
        try {
            Order order = this.orderService.getOrderById(orderId);
            responseDTO.setOrder(order);
            responseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage());
            responseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDTO;
    }

    @PostMapping
    public CreateOrderResponseDTO createOrder(@RequestBody CreateOrderRequestDTO requestDTO) {
        long userId = requestDTO.getUserId();
        List<OrderDetail> orderDetails = requestDTO.getOrderDetails();
        CreateOrderResponseDTO responseDTO = new CreateOrderResponseDTO();
        try {
            Order order = this.orderService.createOrder(userId, orderDetails);
            this.orderService.updateProductQuantity(orderDetails);
            responseDTO.setOrder(order);
            responseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage());
            responseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return  responseDTO;
    }

    @DeleteMapping("/{id}")
    public DeleteOrderResponseDTO deleteOrder(@PathVariable("id") long orderId) {
        DeleteOrderResponseDTO responseDTO = new DeleteOrderResponseDTO();
        try {
            this.orderService.deleteOrder(orderId);
            responseDTO.setMessage("Order with ID: " + orderId + ", has been DELETED");
            responseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            responseDTO.setMessage(e.getMessage());
            responseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDTO;
    }

    @GetMapping("/trending")
    public List<Long> getTrendingProductIds() {
        return this.orderService.getTrendingProductIds();
    }

}
