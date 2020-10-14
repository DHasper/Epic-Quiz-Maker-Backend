package com.quizmaker.backend.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import com.quizmaker.backend.models.Image;
import com.quizmaker.backend.repositories.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final ResourceLoader resourceLoader;
    private final ImageRepository imageRepository;

    private static String IMAGE_DIR = "src/images";

    @Autowired
    public ImageService(ImageRepository imageRepository, ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.imageRepository = imageRepository;
    }

    // get a image using the id
    public Optional<Resource> getImage(int id) {
        Optional<Image> result = imageRepository.findById(id);

        if(result.isPresent()){
            Image image = result.get();
            String name = image.getId() + "_" + image.getName();
            Resource file = resourceLoader.getResource("file:" + IMAGE_DIR + "/" + name);
            return Optional.of(file);
        } else {
            return Optional.empty();
        }     
    }

    // save a image to filesystem
    public Optional<Image> saveImage(MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            Image image = new Image(file.getOriginalFilename());
            imageRepository.save(image);
            Files.copy(file.getInputStream(), Paths.get(IMAGE_DIR, image.getId() + "_" + image.getName()));
            return Optional.of(image);
        } else {
            return Optional.empty();
        }
    }
}