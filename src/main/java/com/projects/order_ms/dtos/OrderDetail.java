package com.projects.order_ms.dtos;

import lombok.Data;

@Data
public class OrderDetail {
    private long productId;
    private int quantity;
}
