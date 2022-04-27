package com.huhoot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
public class FileUploadUtil {

    public void saveFile(String fileName,
                         MultipartFile multipartFile) throws IOException {

        String absolutePath = new FileSystemResource("").getFile().getAbsolutePath();

       // Path uploadPath = Paths.get("/" + getParentDirectoryFromJar() + "/uploads/" + fileName);

        Path uploadPath = Paths.get(absolutePath + "/target/uploads/" + fileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        try (InputStream inputStream = multipartFile.getInputStream()) {

            Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
            log.error(uploadPath.toString());
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public String getParentDirectoryFromJar() {
        String dirtyPath = getClass().getResource("").toString();
        String jarPath = dirtyPath.replaceAll("^.*file:/", ""); //removes file:/ and everything before it
        jarPath = jarPath.replaceAll("jar!.*", "jar"); //removes everything after .jar, if .jar exists in dirtyPath
        jarPath = jarPath.replaceAll("%20", " "); //necessary if path has spaces within
        if (!jarPath.endsWith(".jar")) { // this is needed if you plan to run the app using Spring Tools Suit play button.
            jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
        }
        String directoryPath = Paths.get(jarPath).getParent().toString(); //Paths - from java 8
        return directoryPath;
    }
}