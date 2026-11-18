package com.example.admin.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.admin.DTO.DTOProduck;
import com.example.admin.Service.AdminService;
import com.example.admin.Service.ProduckrService;
import com.example.admin.entity.ProductEntity;

@RestController
@RequestMapping("/admin-dashboard/products")
public class ProduckController {

    private final ProduckrService productService;
    private final AdminService adminService;

    public ProduckController(ProduckrService productService, AdminService adminService) {
        this.productService = productService;
        this.adminService = adminService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@RequestHeader(value = "Authorization", required = false) String authorization,@RequestPart("product") DTOProduck product,@RequestPart("photo") MultipartFile photo) {
        String token = extractBearerToken(authorization);
        if (token == null || !adminService.check_admin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not admin");
        }
        try {
            ProductEntity savedProduct = productService.addProduct(product, photo);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Не удалось сохранить фото товара");
        }
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null || !authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return null;
        }
        return authorization.substring(7).trim();
    }
}
