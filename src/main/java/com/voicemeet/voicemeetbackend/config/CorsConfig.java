package com.voicemeet.voicemeetbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ IMPORTANT: allow all required origins (APK + browser + deployed)
        config.setAllowedOriginPatterns(List.of(
                "http://localhost",                      // 🔥 APK (MOST IMPORTANT)
                "http://localhost:3000",                 // React dev
                "https://localhost",
                "https://voicemeet-frontend.onrender.com"
        ));

        // ✅ allow everything
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ required for Authorization header
        config.setAllowCredentials(true);

        // ✅ cache preflight
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}