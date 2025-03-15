package com.ecommerce.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(String path, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();

        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));

        String randomUUID = UUID.randomUUID().toString();

        String randomFileName = randomUUID + fileType;

        String filePath = path + File.separator + randomFileName;

        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));

        return randomFileName;
    }
    
}
