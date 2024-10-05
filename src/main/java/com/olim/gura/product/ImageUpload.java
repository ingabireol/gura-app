package com.olim.gura.product;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER = "C:\\Users\\ingab\\Desktop\\Springboot\\Ecommerce\\src\\main\\resources\\static\\image-product";
    public String uploadImage(MultipartFile imageProduct){
        String filePath = null;
        try {
            String fileName = imageProduct.getOriginalFilename();
            if(fileName != null){
                Files.copy(imageProduct.getInputStream(),
                        Paths.get(UPLOAD_FOLDER + File.separator,
                                imageProduct.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                filePath = "image-product/" + fileName;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return filePath;
    }
}
