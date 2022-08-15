package com.morse_coders.aucdaisbackend.FileStorage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new RuntimeException("Directory could not be created " + e );
        }
    }

    public String storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try{
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!");
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()){
                return resource;
            }
            else{
                throw new RuntimeException("File not found " + fileName);
            }
        }catch (IOException e){
            throw new RuntimeException("Could not load file " + fileName + ". Please try again!");
        }
    }


    public HttpEntity<String> deleteFile(FileResponse file) {
        Path filePath = this.fileStorageLocation.resolve(file.getFileName()).normalize();
        Path targetfile = this.fileStorageLocation.resolve(filePath.getFileName());
        File myObj = new File(targetfile.getParent().toString() + "/" + targetfile.getFileName().toString());
        System.out.println("Here we are0");
        if(myObj.exists()) {
            System.out.println("Here we are1");
            myObj.delete();
            return new ResponseEntity<>("good", HttpStatus.OK);
        }
        return new ResponseEntity<>("bad", HttpStatus.BAD_REQUEST);
    }
}
