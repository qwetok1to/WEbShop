package com.example.admin.Servises;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.admin.DTO.AddProductRequest;
import com.example.admin.Entity.ProductEntity;
import com.example.admin.Entity.ProductImageEntity;
import com.example.admin.Repository.ProductImageRepository;
import com.example.admin.Repository.ProductRepository;

@Service
public class Servise {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final saveimaga imageStorage;

    public Servise(ProductRepository productRepository,ProductImageRepository productImageRepository,saveimaga imageStorage) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.imageStorage = imageStorage;
    }

    @Transactional
    public ProductEntity addProduct(AddProductRequest request) {
        return productRepository.save(createProduct(request));
    }

    @Transactional
    public ProductEntity addProduct(AddProductRequest request, MultipartFile file) {
        ProductEntity product = productRepository.saveAndFlush(createProduct(request));
        if (file == null || file.isEmpty()) {
            return product;
        }

        String fileName = imageStorage.savephoto(file, request.name());

        try {
            ProductImageEntity image = new ProductImageEntity(product);
            image.setFileName(fileName);
            productImageRepository.saveAndFlush(image);
            product.setImageUrl("/image/" + fileName);
            return productRepository.save(product);
        } catch (RuntimeException exception) {
            try {
                imageStorage.deletePhoto(fileName);
            } catch (RuntimeException cleanupException) {
                exception.addSuppressed(cleanupException);
            }
            throw exception;
        }
    }

    private static ProductEntity createProduct(AddProductRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }
        if (request.price() == null || request.price().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price must be greater than zero");
        }
        if (request.description() != null && request.description().length() > 2000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product description is too long");
        }
        if (request.imageUrl() != null && request.imageUrl().length() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product image URL is too long");
        }

        BigDecimal price;
        try {
            price = request.price().setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price may have at most two decimals",
                    exception);
        }
        if (price.precision() > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price is too large");
        }

        ProductEntity product = new ProductEntity();
        String name = request.name().trim();
        if (name.length() > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is too long");
        }
        product.setName(name);
        product.setDescription(request.description());
        product.setPrice(price);
        product.setImageUrl(request.imageUrl());
        return product;
    }
}
