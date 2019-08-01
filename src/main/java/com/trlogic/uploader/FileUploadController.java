package com.trlogic.uploader;

import com.trlogic.uploader.storage.StorageFileNotFoundException;
import com.trlogic.uploader.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URL;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String index(Model model){
        Long countStored = storageService.getCountStored();

        model.addAttribute("count", "Count stored images: " + countStored);

        return "UploadForm";
    }

    @PostMapping("/")
    public String handleFileUploadMultipart(@Nullable @RequestParam("files") MultipartFile[] files,
                                            @Nullable @RequestParam("imageBase64") String imageBase64,
                                            @Nullable @RequestParam("url") String url,
            RedirectAttributes redirectAttributes) {

        if(files != null){
            for(MultipartFile file : files) {
                storageService.store(file);
            }
        }

        if(imageBase64 != null){
            storageService.store(imageBase64);
        }

        if(url != null){

            try {
                storageService.store(new URL(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        redirectAttributes.addFlashAttribute("message", "Files successfully uploaded !");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
