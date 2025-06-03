package com.project.ecommerce.mapper;

import com.project.ecommerce.dtos.CartDto;
import com.project.ecommerce.dtos.CartItemDto;
import com.project.ecommerce.entities.Cart;
import com.project.ecommerce.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items",source = "items")
    @Mapping(target = "totalPrice",expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);
    @Mapping(target = "totalPrice",expression = "java(cartItem.getTotalPrice())")
    @Mapping(target = "product.id", source = "product.id")
    CartItemDto toItemDto(CartItem cartItem);
}
