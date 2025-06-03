package com.project.ecommerce.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull
    @Min(value = 1,message = "Quantity could not be less than 1")
    @Max(value = 1000,message = "Quantity must not be more than 1000")
    private Integer quantity;
}
