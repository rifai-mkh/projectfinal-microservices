package com.rapidtech.productservices.repository;

import com.rapidtech.productservices.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByProductCodeIn(List<String> productCode);
}
