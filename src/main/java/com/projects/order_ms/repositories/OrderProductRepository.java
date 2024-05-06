package com.projects.order_ms.repositories;

import com.projects.order_ms.models.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> { }
