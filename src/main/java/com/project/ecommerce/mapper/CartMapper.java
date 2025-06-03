package com.project.ecommerce.mapper;

import com.project.ecommerce.dtos.CartDto;
import com.project.ecommerce.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
