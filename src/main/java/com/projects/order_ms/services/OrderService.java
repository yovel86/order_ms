package com.projects.order_ms.services;

import com.projects.order_ms.dtos.OrderDetail;
import com.projects.order_ms.dtos.ProductDTO;
import com.projects.order_ms.exceptions.OrderNotFoundException;
import com.projects.order_ms.exceptions.ProductNotAvailableException;
import com.projects.order_ms.models.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(long orderId) throws OrderNotFoundException;

    Order createOrder(long userId, List<OrderDetail> orderDetails) throws ProductNotAvailableException;

    void deleteOrder(long orderId) throws OrderNotFoundException;

    void updateOrderProducts(List<OrderDetail> orderDetails, Order order);

    void updateProductQuantity(List<OrderDetail> orderDetails);

}
