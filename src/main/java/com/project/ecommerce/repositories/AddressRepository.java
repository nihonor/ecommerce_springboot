package com.project.ecommerce.repositories;

import com.project.ecommerce.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}