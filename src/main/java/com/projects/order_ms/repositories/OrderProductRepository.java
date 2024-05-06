package com.projects.order_ms.repositories;

import com.projects.order_ms.models.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op from OrderProduct AS op ORDER BY op.id DESC LIMIT  10")
    List<OrderProduct> findTrendingProducts();

}
