package com.example.admin.Controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.admin.DTO.AddProductRequest;
import com.example.admin.Entity.ProductEntity;
import com.example.admin.Servises.Servise;
import com.example.admin.Servises.saveimaga;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Servise productService;
    private final saveimaga imageStorage;

    public ProductController(Servise productService, saveimaga imageStorage) {
        this.productService = productService;
        this.imageStorage = imageStorage;
    }

    @PostMapping
    public ResponseEntity<ProductEntity> addProduct(@RequestBody AddProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(request));
    }

    @PostMapping(path = "/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductEntity> addProductWithImage(
            @RequestPart("product") AddProductRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(request, file));
    }

    @PostMapping(path = "/AddPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageStorage.savephoto(file, description));
    }

}
