package tn.esprit.se.pispring.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

 @Configuration
    public class couldinaryConfig {
        @Value("${cloudinary.cloud-name}")
        private String cloudName;

        @Value("${cloudinary.api-key}")
        private String apiKey;

        @Value("${cloudinary.api-secret}")
        private String apiSecret;

        @Bean
        public Cloudinary cloudinary(){
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", cloudName);
            config.put("api_key", apiKey);
            config.put("api_secret", apiSecret);

            return new Cloudinary(config);
        }
    }

