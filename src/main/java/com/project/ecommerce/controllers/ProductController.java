package com.project.ecommerce.controllers;

import com.project.ecommerce.dtos.ProductDto;
import com.project.ecommerce.entities.Product;
import com.project.ecommerce.mapper.ProductMapper;
import com.project.ecommerce.repositories.CategoryRepository;
import com.project.ecommerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false,name = "categoryId") Byte categoryId
    ) {
        List<Product> products;
        if(categoryId != null) {
            products=productRepository.findByCategoryId(categoryId);
        }
        else {
            products=productRepository.findAllWithCategory();
        }


        return products
                .stream()
                .map(productMapper::toDto)
                .toList();



    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
       var product = productRepository.findById(id).orElse(null);
       if (product == null) {
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            UriComponentsBuilder uriBuilder,
            @RequestBody ProductDto productDto) {
        var category=categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        var uri=uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductDto productDto
    ){

            var product=productRepository.findById(id).orElse(null);
            var category=categoryRepository.findById(productDto.getCategoryId()).orElse(null);
            if(category == null) {
                return ResponseEntity.badRequest().build();
            }
            if(product == null) {
                return ResponseEntity.notFound().build();
            }
            productMapper.update(productDto, product);
            product.setCategory(category);
            productRepository.save(product);
            productDto.setId(product.getId());
            return ResponseEntity.ok(productDto);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product=productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }


}
