package com.project.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts", schema = "ecommerce")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 16)
    private UUID id;


    @Column(name = "date_created",insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.MERGE, fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice(){
        BigDecimal total = BigDecimal.ZERO;
        for(CartItem item : items){
            total = total.add(item.getTotalPrice());
        }
        return total;
    }

    public CartItem getItem(Long productId){
        return items.stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

    }

    public CartItem addItem(Product product){
        var cartItem=getItem(product.getId());
        if(cartItem!=null){
            cartItem.setQuantity(cartItem.getQuantity()+1);
        }
        else {
            cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            items.add(cartItem);
        }
        return cartItem;
    }

    public void removeItem(Long productId){
        var carItem=getItem(productId);
        if(carItem!=null){
            items.remove(carItem);
        }
        carItem.setCart(null);
    }

    public void clearCart(){
        items.clear();
    }



}