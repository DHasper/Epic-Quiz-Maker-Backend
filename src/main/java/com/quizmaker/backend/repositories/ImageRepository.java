package com.quizmaker.backend.repositories;

import com.quizmaker.backend.models.Image;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImageRepository extends PagingAndSortingRepository<Image, Integer> {

    public Image findByName(String name);
    
}