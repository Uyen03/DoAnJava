package com.example.DoAnJaVa;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Thay đổi đường dẫn tuyệt đối phù hợp với cấu trúc thư mục của bạn
        String absolutePath = System.getProperty("user.dir") + "/product-images/";
        registry.addResourceHandler("/product-images/**")
                .addResourceLocations("file:" + absolutePath);
    }
}