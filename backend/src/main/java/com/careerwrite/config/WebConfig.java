package com.careerwrite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Our frontend is plain HTML/CSS/JS opened directly in the browser (or served
// from a different port than 8080), so the browser blocks fetch() calls to
// our API unless we explicitly allow it. This class allows all origins,
// which is fine for a college project running on localhost.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
