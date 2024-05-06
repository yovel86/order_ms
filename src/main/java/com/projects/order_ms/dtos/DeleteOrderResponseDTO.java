package com.projects.order_ms.dtos;

import lombok.Data;

@Data
public class DeleteOrderResponseDTO {
    private String message;
    private ResponseStatus responseStatus;
}
