package com.example.admin.Servises;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class saveimaga {

    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/gif", "gif");

    private final Path path;

    public saveimaga(@Value("${app.image.storage-dir:image}") String storageDirectory) {
        this.path = Path.of(storageDirectory).toAbsolutePath().normalize();
    }

    public String savephoto(MultipartFile file, String description) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();
        String extension = contentType == null
                ? null
                : ALLOWED_TYPES.get(contentType.toLowerCase(Locale.ROOT));
        if (extension == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only JPEG, PNG, and GIF images are supported");
        }

        byte[] contents;
        try {
            contents = file.getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Could not read uploaded image", e);
        }

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(contents));
            if (image == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is not a valid image");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is not a valid image", e);
        }

        String safeDescription = description == null || description.isBlank()
                ? "product"
                : description.replaceAll("[^a-zA-Z0-9а-яА-ЯёЁ_-]", "_");
        String fileName = safeDescription + "_" + UUID.randomUUID() + "." + extension;
        Path filePath = path.resolve(fileName).normalize();
        if (!filePath.getParent().equals(path)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image file name");
        }

        try {
            Files.createDirectories(path);
            Files.write(filePath, contents);
            return fileName;
        } catch (IOException e) {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException cleanupException) {
                e.addSuppressed(cleanupException);
            }
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Could not store uploaded image", e);
        }
    }

    public void deletePhoto(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }

        Path filePath = path.resolve(fileName).normalize();
        if (!filePath.getParent().equals(path)) {
            throw new IllegalArgumentException("Invalid stored image file name");
        }

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Could not remove uploaded image", e);
        }
    }
}
