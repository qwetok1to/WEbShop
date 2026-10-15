package com.example.admin.DTO;

import java.math.BigDecimal;

public record AddProductRequest(String name, String description, BigDecimal price, String imageUrl) {

}