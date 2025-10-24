//package com.reddit.clone.config;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Configuration
//public class CloudinaryConfig {
//    private final Cloudinary cloudinary;
//
//    @Bean
//    public Cloudinary cloudinary() {
//        return new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "dea2b83gj",
//                "api_key", "495534623496618",
//                "api_secret", "AzcDc0EpcmYi15FFGxPP_CfiBoA",
//                "secure", true
//        ));
//    }
//    public String uploadFile(MultipartFile file) {
//        try {
//            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//            return uploadResult.get("secure_url").toString();
//        } catch (IOException e) {
//            log.error("Cloudinary upload error", e);
//            throw new RuntimeException("Failed to upload file", e);
//        }
//    }
//}
