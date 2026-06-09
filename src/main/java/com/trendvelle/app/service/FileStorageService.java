package com.trendvelle.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads/products}")
    private String uploadDir;

    /**
     * Saves the uploaded file and returns the web-accessible path like /uploads/products/abc.jpg
     */
    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        // Validate extension
        if (!ext.matches("\\.(jpg|jpeg|png|gif|webp)")) {
            throw new IllegalArgumentException("Only image files are allowed (jpg, png, gif, webp).");
        }

        String filename = UUID.randomUUID().toString() + ext;

        // Store relative to working directory so it works portably
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/" + uploadDir + "/" + filename;
    }

    public void delete(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) return;
        try {
            // Strip leading slash
            String relative = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;
            Path p = Paths.get(relative);
            Files.deleteIfExists(p);
        } catch (IOException ignored) {}
    }
}
