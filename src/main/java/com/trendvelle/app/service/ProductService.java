package com.trendvelle.app.service;

import com.trendvelle.app.model.Category;
import com.trendvelle.app.model.Product;
import com.trendvelle.app.repository.CategoryRepository;
import com.trendvelle.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    public List<Product> getAllActive() { return productRepository.findByActiveTrue(); }
    public List<Product> getAll()       { return productRepository.findAll(); }

    public List<Product> getByCategory(Long catId) {
        return categoryRepository.findById(catId)
                .map(productRepository::findByCategoryAndActiveTrue)
                .orElseGet(this::getAllActive);
    }

    public List<Product> search(String q) { return productRepository.search(q); }
    public Optional<Product> findById(Long id) { return productRepository.findById(id); }
    public List<Category> getAllCategories() { return categoryRepository.findAll(); }

    @Transactional
    public Product saveProduct(String name, String description, BigDecimal price,
                               Integer stock, String sizes, String icon,
                               Long categoryId, MultipartFile imageFile) {
        Category cat = categoryRepository.findById(categoryId).orElseThrow();
        String imagePath = null;
        try {
            imagePath = fileStorageService.store(imageFile);
        } catch (Exception ignored) {}

        return productRepository.save(Product.builder()
                .name(name).description(description).price(price)
                .stockQuantity(stock).sizes(sizes).icon(icon)
                .imagePath(imagePath).category(cat).active(true).build());
    }

    @Transactional
    public Product updateProduct(Long id, String name, String description, BigDecimal price,
                                 Integer stock, String sizes, String icon,
                                 Long categoryId, MultipartFile imageFile) {
        Product p = productRepository.findById(id).orElseThrow();
        Category cat = categoryRepository.findById(categoryId).orElseThrow();

        // Only replace image if a new one is uploaded
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image
            fileStorageService.delete(p.getImagePath());
            try {
                p.setImagePath(fileStorageService.store(imageFile));
            } catch (Exception ignored) {}
        }

        p.setName(name); p.setDescription(description); p.setPrice(price);
        p.setStockQuantity(stock); p.setSizes(sizes); p.setIcon(icon);
        p.setCategory(cat);
        return productRepository.save(p);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(p -> {
            fileStorageService.delete(p.getImagePath());
            p.setActive(false);
            productRepository.save(p);
        });
    }

    public long countProducts() { return productRepository.findByActiveTrue().size(); }
}
