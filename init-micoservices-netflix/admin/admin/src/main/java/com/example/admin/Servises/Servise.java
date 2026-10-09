package com.example.admin.Servises;

import java.math.BigDecimal;


import org.springframework.stereotype.Service;



import com.example.admin.DTO.add_product;
import com.example.admin.Entity.ProductEntity;
import com.example.admin.Repository.ProductRepository;

@Service
public class Servise {

    private final ProductRepository productRepository;

    public Servise(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public ProductEntity addProduct(add_product request) {
      

        ProductEntity product = new ProductEntity();
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(BigDecimal.valueOf(request.price()));
        product.setImageUrl(request.imageUrl());
        return productRepository.save(product);
    }
}
