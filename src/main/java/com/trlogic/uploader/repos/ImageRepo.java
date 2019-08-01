package com.trlogic.uploader.repos;

import com.trlogic.uploader.repos.entities.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepo extends CrudRepository<Image, Integer> {
}
