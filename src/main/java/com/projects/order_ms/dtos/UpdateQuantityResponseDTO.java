package com.projects.order_ms.dtos;

import lombok.Data;

@Data
public class UpdateQuantityResponseDTO {
    private ProductDTO productDTO;
    private String message;
    private ResponseStatus responseStatus;
}
