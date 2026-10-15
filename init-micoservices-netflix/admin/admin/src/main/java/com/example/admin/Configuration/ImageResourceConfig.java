package com.example.admin.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.admin.Servises.saveimaga;

@Configuration
public class ImageResourceConfig implements WebMvcConfigurer {

    private final saveimaga imageStorage;

    public ImageResourceConfig(saveimaga imageStorage) {
        this.imageStorage = imageStorage;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = imageStorage.getStorageDirectory().toUri().toString();
        if (!location.endsWith("/")) {
            location += "/";
        }
        registry.addResourceHandler("/image/**").addResourceLocations(location);
    }
}
