package com.hppm.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.util.StringUtils;

public class FileUtils {
    
    public static String renameFileByUUID(String originalFilename) {
        String newFileName = UUID.randomUUID().toString();
        String extension = FileUtils.extractExtension(originalFilename);
        if (!StringUtils.isEmpty(extension)) {
            newFileName += extension; // 这里的 extension 带点
        }
        return newFileName;
    }
    
    public static String extractFilename(String filename) {
        
        int bsPos = filename.lastIndexOf('\\');
        int fsPos = filename.lastIndexOf('/');
        if (bsPos >= 0 || fsPos >= 0) {
            if (fsPos == -1 || bsPos > fsPos) {
                return filename.substring(bsPos + 1);
            }
            return filename.substring(fsPos + 1);
        }
        return filename;
    }

    public static String extractExtension(String filename) {
        if (filename != null) {
            int dPos = filename.lastIndexOf('.');
            if (dPos >= 0) {
                return filename.substring(dPos);
            }
        }
        return "";
    }

    public static void createDirectoriesIfNotExist(String path) {
        Path p = Paths.get(path);

        try {
            if (Files.notExists(p)) {
                Files.createDirectories(p);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create directories for " + path);
        }
    }
}
