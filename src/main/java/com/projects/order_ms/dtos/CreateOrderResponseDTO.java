package com.projects.order_ms.dtos;

import com.projects.order_ms.models.Order;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderResponseDTO {
    private Order order;
    private String message;
    private ResponseStatus responseStatus;
}
