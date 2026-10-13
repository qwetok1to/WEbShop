package com.example.admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.admin.Entity.ProductImageEntity;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
}
