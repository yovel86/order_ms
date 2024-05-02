package com.projects.order_ms.models;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "orders")
public class Order extends BaseModel {
    private long customerId;
    private double totalAmount;
    private Date orderDate;
}
