package com.projects.order_ms.models;

import jakarta.persistence.*;
import lombok.Data;

@Data @Entity
public class OrderProduct extends BaseModel {
    private long orderId;
    private long productId;
    private int quantity;
}
