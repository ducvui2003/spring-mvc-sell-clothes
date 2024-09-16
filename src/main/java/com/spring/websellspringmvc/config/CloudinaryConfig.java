package com.spring.websellspringmvc.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.spring.websellspringmvc.properties.CloudinaryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap("cloud_name", CloudinaryProperties.getCloudName(), "api_key", CloudinaryProperties.getApiKey(), "api_secret", CloudinaryProperties.getApiSecret(), "access_mode", "public", "secure", true));
    }

    @Bean
    Transformation transformation() {
        return new Transformation().crop("scale").chain()
                .quality("auto").chain()
                .fetchFormat("auto");
    }
}
