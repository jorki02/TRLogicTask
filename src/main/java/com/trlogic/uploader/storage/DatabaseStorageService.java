package com.trlogic.uploader.storage;

import com.trlogic.uploader.repos.ImageRepo;
import com.trlogic.uploader.repos.entities.Image;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;

@Transactional
@Service
public class DatabaseStorageService implements StorageService {

    @Autowired
    ImageRepo imageRepo;

    @Override
    public void store(MultipartFile file) {
        try {
            String base64EncodedImage = Base64.getEncoder().encodeToString(file.getBytes());
            store(base64EncodedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void store(String base64EncodedImage) {
        Image image = new Image();
        image.setImage(base64EncodedImage);

        imageRepo.save(image);
    }

    @Override
    public void store(URL url) throws IOException {
        InputStream input = url.openStream();
        byte[] imageBytes = IOUtils.toByteArray(input);
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);
        store(base64EncodedImage);
    }

    @Override
    public Long getCountStored() {
        return imageRepo.count();
    }

}
