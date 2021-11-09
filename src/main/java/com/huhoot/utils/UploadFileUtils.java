package com.huhoot.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class UploadFileUtils {

    public String uploadImage(String originalFileName, String base64) throws IOException {

        String imagesPath = this.getDirectPath();

        File folder = new File(imagesPath);

        if (!folder.exists()) {
            folder.mkdir();
        }

        String filename = this.generateFileName(originalFileName);

        byte[] bytes = Base64.decodeBase64(base64);

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(imagesPath + "/" + filename))) {
            fileOutputStream.write(bytes);
        } catch (IOException e) {

            e.printStackTrace();
            return "default-challenge-cover-image.png";

        }
        return filename;
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    private String getDirectPath() throws IOException {
        Path uploadDir = Paths.get("resources");
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        return uploadPath;
    }
}
