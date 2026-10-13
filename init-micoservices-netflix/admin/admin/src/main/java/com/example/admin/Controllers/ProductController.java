package com.example.admin.Controllers;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductEntity> addProduct(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        add_product request = new add_product(
                name, description, price.doubleValue(), null);
        ProductEntity savedProduct = productService.addProduct(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
}
