package com.trendvelle.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/products}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded product images at /uploads/products/**
        Path uploadPath = Paths.get(uploadDir);
        String uploadAbsolute = uploadPath.toFile().getAbsolutePath();

        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:" + uploadAbsolute + "/");

        // Keep the default static resource mapping too
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}
