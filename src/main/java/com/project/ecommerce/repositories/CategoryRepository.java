package com.project.ecommerce.repositories;

import com.project.ecommerce.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}