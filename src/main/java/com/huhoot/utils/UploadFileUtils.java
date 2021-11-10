package com.huhoot.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class UploadFileUtils {

    public String uploadImage(String originalFileName, String base64) throws IOException {

        Path uploadPath = Paths.get("../../tmp/resources/");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = this.generateFileName(originalFileName);

        byte[] bytes = Base64.decodeBase64(base64);

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(uploadPath + "/" + filename))) {
            fileOutputStream.write(bytes);
        } catch (IOException e) {

            e.printStackTrace();
            return "default-challenge-cover-image.png";

        }
        return filename;
    }

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    private String getDirectPath() throws IOException {
        Path uploadDir = Paths.get("images");
        // String uploadPath = uploadDir.toFile().getAbsolutePath();
        String uploadPath = new File(".").getCanonicalPath() + "/src/main/resources/images/";
        return uploadPath;
    }
}
