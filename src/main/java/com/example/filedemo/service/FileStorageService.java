package com.example.filedemo.service;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.payload.DownloadFileResponse;
import com.example.filedemo.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final List<DownloadFileResponse> listDownloadFile;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, List<DownloadFileResponse> listDownloadFile) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.listDownloadFile = listDownloadFile;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        System.out.println("GO HERE");

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            DownloadFileResponse downloadFileResponse = new DownloadFileResponse();
            String uniqueID1 = UUID.randomUUID().toString();
            System.out.println("========uniqueID1 : " + uniqueID1);
            downloadFileResponse.setId(uniqueID1);
            System.out.println("randomUniqueId : " + downloadFileResponse.getId());

            String logo = "https://www.elcom.com.vn/wp-content/uploads/2020/09/logo-elcom_orig.png";
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();
            String nameGame = fileName.substring(0,fileName.length()-4);
            downloadFileResponse.setNameGame(nameGame);
            downloadFileResponse.setUrlDownload(fileDownloadUri);
            downloadFileResponse.setLogo(logo);
            listDownloadFile.add(downloadFileResponse);
            System.out.println("fileDownloadUri : " + fileDownloadUri);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {


                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    public List<DownloadFileResponse> getListFileFolderDownload(){
        return listDownloadFile;
    }
}
