package com.project.ecommerce.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItems = new LinkedHashSet<>();

}