package com.project.ecommerce.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartProductRequest {
    @NotNull
    private Long productId;
}
