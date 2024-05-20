package com.projects.order_ms.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ListResponse {
    private List<ProductDTO> products;
    private String message;
    private ResponseStatus responseStatus;
}