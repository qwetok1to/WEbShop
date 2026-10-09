package com.example.admin.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.admin.DTO.add_product;
import com.example.admin.Entity.ProductEntity;
import com.example.admin.Servises.Servise;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Servise productService;

    public ProductController(Servise productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductEntity> addProduct(@RequestBody add_product request) {
        ProductEntity savedProduct = productService.addProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
}
