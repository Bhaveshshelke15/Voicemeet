package com.voicemeet.voicemeetbackend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "di12zwm9z",
                "api_key", "974542192496432",
                "api_secret", "_DCbnJu0rmv0b0ZGm-juZGdKw6M"
        ));
    }
}
