package com.huhoot.config;

import com.huhoot.utils.FileUploadUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Slf4j
@AllArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    private final FileUploadUtil fileUploadUtil;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("uploads", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {

        String uploadPath = "/" + fileUploadUtil.getParentDirectoryFromJar() + "/uploads/";

        log.error("upload path " + uploadPath);

        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");



        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}