//package com.reddit.clone.service;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//@Slf4j
//public class CloudinaryService {
//
//    private final Cloudinary cloudinary;
//
//    public CloudinaryService() {
//        this.cloudinary =
//         new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "dea2b83gj",
//                "api_key", "495534623496618",
//                "api_secret", "AzcDc0EpcmYi15FFGxPP_CfiBoA",
//                "secure", true
//        ));
//    }
//
//    public String uploadFile(MultipartFile file) {
//        try {
//            // Prepare parameters to explicitly allow auto-detection of resource type
//            Map<String, Object> params = ObjectUtils.asMap(
//                    "resource_type", "auto"
//            );
//
//            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
//
//            // Log the successful upload details for debugging
//            log.info("Cloudinary upload successful. Resource type: {}, URL: {}",
//                    uploadResult.get("resource_type"), uploadResult.get("secure_url"));
//
//            return uploadResult.get("secure_url").toString();
//        } catch (IOException e) {
//            log.error("Cloudinary upload error", e);
//            throw new RuntimeException("Failed to upload file. Check file type and size.", e);
//        }
//    }
//}
package com.reddit.clone.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryService {

    // INJECT the Cloudinary bean created by CloudinaryConfig
    private final Cloudinary cloudinary;

    // Use constructor injection
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) {
        try {
            // Prepare parameters (resource_type: auto is correct for videos/images/gifs)
            Map<String, Object> params = ObjectUtils.asMap(
                    "resource_type", "auto"
            );

            // Cloudinary instance is already configured, just use it
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

            log.info("Cloudinary upload successful. Resource type: {}, URL: {}",
                    uploadResult.get("resource_type"), uploadResult.get("secure_url"));

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            log.error("Cloudinary upload error", e);
            throw new RuntimeException("Failed to upload file. Check file type and size.", e);
        }
    }
}
