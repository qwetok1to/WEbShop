package com.example.admin.Service;

import com.example.admin.DTO.DTOProduck;
import com.example.admin.entity.ProductEntity;
import com.example.admin.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class ProduckrService {

    private static final Map<String, String> IMAGE_EXTENSIONS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/gif", ".gif",
            "image/webp", ".webp"
    );

    private final ProductRepository productRepository;
    private final Path uploadDirectory;

    public ProduckrService(ProductRepository productRepository,
                           @Value("${product.upload-dir:uploads/products}") String uploadDirectory) {
        this.productRepository = productRepository;
        this.uploadDirectory = Paths.get(uploadDirectory).toAbsolutePath().normalize();
    }

    public ProductEntity addProduct(DTOProduck product, MultipartFile photo) throws IOException {
        if (product == null || product.name == null || product.name.isBlank()) {
            throw new IllegalArgumentException("Название товара обязательно");
        }
        if (product.sell <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше нуля");
        }
        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("Фотография товара обязательна");
        }

        String extension = IMAGE_EXTENSIONS.get(photo.getContentType());
        if (extension == null) {
            throw new IllegalArgumentException("Поддерживаются изображения JPEG, PNG, GIF и WebP");
        }

        Files.createDirectories(uploadDirectory);
        String filename = UUID.randomUUID() + extension;
        Path target = uploadDirectory.resolve(filename).normalize();
        if (!target.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Недопустимое имя файла");
        }

        photo.transferTo(target);
        try {
            ProductEntity entity = new ProductEntity(
                    product.name.trim(), product.sell, filename, product.description);
            return productRepository.save(entity);
        } catch (RuntimeException e) {
            Files.deleteIfExists(target);
            throw e;
        }
    }
}
