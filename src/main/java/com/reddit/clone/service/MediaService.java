//package com.reddit.clone.service;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//public class MediaService {
//
//    private final Cloudinary cloudinary;
//    public MediaService(Cloudinary cloudinary) {
//        this.cloudinary = cloudinary;
//    }
//
//    public String uploadMedia(MultipartFile file) throws IOException {
//        Map<String, Object> uploadResult;
//        uploadResult = cloudinary.uploader().upload(file.getBytes(),  // Skip the Sonar Cube Error
//                ObjectUtils.asMap(
//                        "resource_type", "auto"
//                ));
//        return uploadResult.get("secure_url").toString(); // Return URL to frontend
//    }
//}
