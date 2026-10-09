package com.example.admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.admin.Entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
