package com.example.admin.Servises;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class saveimaga {
    private final Path storageDirectory;

    public saveimaga(@Value("${app.image.storage-dir:image}") String storageDirectory) {
        this.storageDirectory = Path.of(storageDirectory).toAbsolutePath().normalize();
    }

    public String savephoto(MultipartFile file, String description) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An image file is required");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = getSafeExtension(originalFilename);
            String safeDescription = description == null || description.isBlank()
                    ? "photo"
                    : description.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
            if (safeDescription.length() > 40) {
                safeDescription = safeDescription.substring(0, 40);
            }
            String fileName = safeDescription + "_" + UUID.randomUUID() + extension;
            Path filePath = resolveFile(fileName);
            Files.createDirectories(storageDirectory);
            try (var input = file.getInputStream()) {
                Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return fileName;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save image", e);
        }
    }

    public void deletePhoto(String fileName) {
        try {
            Files.deleteIfExists(resolveFile(fileName));
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось удалить файл: " + fileName, e);
        }
    }

    public Path getStorageDirectory() {
        return storageDirectory;
    }

    private Path resolveFile(String fileName) {
        Path resolved = storageDirectory.resolve(fileName).normalize();
        if (!resolved.startsWith(storageDirectory)) {
            throw new IllegalArgumentException("Invalid image file name");
        }
        return resolved;
    }

    private static String getSafeExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            return "";
        }
        String leafName = originalFilename.replace('\\', '/');
        leafName = leafName.substring(leafName.lastIndexOf('/') + 1);
        int extensionIndex = leafName.lastIndexOf('.');
        if (extensionIndex < 0) {
            return "";
        }
        String extension = leafName.substring(extensionIndex);
        return extension.matches("\\.[a-zA-Z0-9]{1,10}") ? extension.toLowerCase() : "";
    }
}
