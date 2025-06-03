package com.project.ecommerce.controllers;

import com.project.ecommerce.dtos.AddCartProductRequest;
import com.project.ecommerce.dtos.CartDto;
import com.project.ecommerce.dtos.CartItemDto;
import com.project.ecommerce.dtos.UpdateCartItemRequest;
import com.project.ecommerce.exception.CartNotFoundException;
import com.project.ecommerce.exception.ProductNotFoundException;
import com.project.ecommerce.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name = "carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cartDto=cartService.createCart();
        var uri=uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
}

@Operation(summary = "Adding product to cart")
@PostMapping("/{cartId}/items")
public ResponseEntity<CartItemDto> addToCart(
       @RequestBody AddCartProductRequest request,
       @PathVariable UUID cartId
){
        var cartItemDto=cartService.addCartItem(cartId,request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
}
@GetMapping("/{cartId}")
public ResponseEntity<CartDto> getCart(@PathVariable("cartId") UUID cartId) {
       var cartDto=cartService.getCartItem(cartId);
        return ResponseEntity.ok(cartDto);
}
@PutMapping("/{cartId}/items/{productId}")
public ResponseEntity<?> updateCartItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId,
        @Valid @RequestBody UpdateCartItemRequest request
        ){

        var cartItem=cartService.updateCartItem(cartId,productId,request.getQuantity());
        return ResponseEntity.ok(cartItem);

        }

@DeleteMapping("/{cartId}/items/{productId}")
public ResponseEntity<?> deleteCartItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable Long productId
) {
      cartService.deleteCartItem(cartId,productId);
        return ResponseEntity.noContent().build();

        }

        @DeleteMapping("/{cartId}/items")
        public ResponseEntity<Void> deleteCart(
                @PathVariable("cartId") UUID cartId
        ){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();

        }

        @ExceptionHandler(CartNotFoundException.class)
        public ResponseEntity<Map<String,String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Cart not found"));
        }

        @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<Map<String,String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Product not found in the cart"));
        }


}
