package com.projects.order_ms.dtos;

import com.projects.order_ms.models.Order;
import lombok.Data;

@Data
public class GetOrderResponseDTO {
    private Order order;
    private String message;
    private ResponseStatus responseStatus;
}
