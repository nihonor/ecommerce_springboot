package com.project.ecommerce.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto {
    private Long id;
    private String name;
    private int price;

}
