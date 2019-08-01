package com.trlogic.uploader.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

public interface StorageService {
    void store(MultipartFile file);
    void store(String base64);
    void store(URL url) throws IOException;

    Long getCountStored();
}
