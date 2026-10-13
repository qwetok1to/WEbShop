package com.example.admin.Servises;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.admin.DTO.add_product;
import com.example.admin.Entity.ProductEntity;
import com.example.admin.Entity.ProductImageEntity;
import com.example.admin.Repository.ProductImageRepository;
import com.example.admin.Repository.ProductRepository;

@Service
public class Servise {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final saveimaga imageStorage;

    public Servise(
            ProductRepository productRepository,
            ProductImageRepository productImageRepository,
            saveimaga imageStorage) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.imageStorage = imageStorage;
    }

    public ProductEntity addProduct(add_product request) {
        return productRepository.save(createProduct(request));
    }

    @Transactional
    public ProductEntity addProduct(add_product request, MultipartFile file) {
        ProductEntity product = productRepository.saveAndFlush(createProduct(request));
        String fileName = imageStorage.savephoto(file, request.name());
        if (fileName == null) {
            return product;
        }

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

    private static ProductEntity createProduct(add_product request) {
        ProductEntity product = new ProductEntity();
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(BigDecimal.valueOf(request.price()));
        product.setImageUrl(request.imageUrl());
        return product;
    }
}
