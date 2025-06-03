package com.project.ecommerce.services;

import com.project.ecommerce.dtos.CartDto;
import com.project.ecommerce.dtos.CartItemDto;
import com.project.ecommerce.entities.Cart;
import com.project.ecommerce.exception.CartNotFoundException;
import com.project.ecommerce.exception.ProductNotFoundException;
import com.project.ecommerce.mapper.CartMapper;
import com.project.ecommerce.repositories.CartRepository;
import com.project.ecommerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;
    public CartDto createCart() {
        var cart=new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addCartItem(UUID cartId,Long productId) {
        var cart=cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart==null){
            throw  new CartNotFoundException();
        }

        var product=productRepository.findById(productId).orElse(null);
        if(product==null){
            throw  new ProductNotFoundException();
        }

        var cartItem=cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toItemDto(cartItem);
    }

    public CartDto getCartItem(UUID cartId) {
        var cart=cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart==null){
            throw  new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId,Long productId,Integer quantity) {
        var cart=cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart==null){
            throw  new CartNotFoundException();
        }
        var cartItem=cart.getItem(productId);
        if(cartItem==null){
           throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toItemDto(cartItem);
    }
    public void deleteCartItem(UUID cartId,Long productId) {
        var cart=cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart==null){
           throw new CartNotFoundException();
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
    }
    public void clearCart(UUID cartId) {
        var cart=cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart==null){
         throw new CartNotFoundException();
        }
        cart.clearCart();
        cartRepository.save(cart);
    }
}
