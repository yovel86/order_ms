package com.projects.order_ms.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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
}
