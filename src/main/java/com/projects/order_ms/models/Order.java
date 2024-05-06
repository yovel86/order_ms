package com.projects.order_ms.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity(name = "orders")
public class Order extends BaseModel {
    private long userId;
    private double totalAmount;
    private Date orderDate;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @OneToMany
    private List<OrderProduct> orderProducts;
}
