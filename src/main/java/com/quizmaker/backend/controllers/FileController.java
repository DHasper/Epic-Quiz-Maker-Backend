package com.quizmaker.backend.controllers;

import java.io.IOException;
import java.util.Optional;

import com.quizmaker.backend.models.Image;
import com.quizmaker.backend.services.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller with routes for uploading and retrieving files for the server.
 */
@RestController
@CrossOrigin()
public class FileController {

    @Autowired
    private ImageService imageService;

    /**
     * Uploads a image to the data base and stores it on the disk.
     * 
     * @param MultipartFile containing the image.
     * @return Image object with the newly created image record.
     */
    @PostMapping("/image")
    public ResponseEntity<Image> login(@RequestParam(name = "file") final MultipartFile file) {
       
        Image image;

        try {
            Optional<Image> result = imageService.saveImage(file);
            if(!result.isPresent()){
                // BAD REQUEST if image cannot be saved
                return ResponseEntity.badRequest().body(null);
            } 

            image = result.get();

        } catch (IOException e) {
            // BAD REQUEST when the given image cannot be written to disk
            return ResponseEntity.badRequest().body(null);
        }

        // OK
        return ResponseEntity.ok().body(image);
    }

    /**
     * Retrieves images with a given id.
     * 
     * @param id used to find the image.
     * @return InputStreamResource with the image.
     * @throws IOException if something went wrong retrieving image from disk
     */
    @GetMapping("image/{id}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable final int id) throws IOException {
        Optional<Resource> result = imageService.getImage(id);

        // check if image returned
        if(result.isPresent()){
            // OK
            return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(new InputStreamResource(result.get().getInputStream()));
        } else {
            // NOT FOUND if no image found with id
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}