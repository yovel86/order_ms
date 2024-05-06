package com.projects.order_ms.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GetProductsRequestDTO {
    private List<Long> productIds;
}
