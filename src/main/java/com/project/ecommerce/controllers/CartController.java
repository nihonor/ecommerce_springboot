package com.project.ecommerce.controllers;

import com.project.ecommerce.dtos.CartDto;
import com.project.ecommerce.entities.Cart;
import com.project.ecommerce.mapper.CartMapper;
import com.project.ecommerce.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private CartRepository cartRepository;
    private CartMapper cartMapper;


    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cart=new Cart();
        cartRepository.save(cart);
        var cartDto=cartMapper.toDto(cart);
        var uri=uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
}
}
