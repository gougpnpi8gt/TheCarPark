package com.work.thecarpark.controllers;

import com.work.thecarpark.service.ServiceGps;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/file")
public class UploadController {
    final ServiceGps serviceGps;

    @Autowired
    public UploadController(ServiceGps serviceGps) {
        this.serviceGps = serviceGps;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            double distance = serviceGps.processFile(file);
            return ResponseEntity.ok("Пройденное расстояние: " + distance + " км");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Ошибка при обработке файла: " + e.getMessage());
        }
    }
}