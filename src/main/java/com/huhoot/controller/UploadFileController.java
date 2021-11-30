package com.huhoot.controller;

import com.huhoot.utils.FileUploadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("host")
public class UploadFileController {


    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));

        String uploadDir = "uploads/";

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return ResponseEntity.ok(fileName);
    }
}