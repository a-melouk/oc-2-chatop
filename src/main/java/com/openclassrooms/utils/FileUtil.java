package com.openclassrooms.utils;


import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
    /**
     * Extracts the original file name from a MultipartFile
     *
     * @param file The MultipartFile to extract the name from
     * @return The original file name
     */
    public static String extractFileName(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            // Remove path information if present (some browsers include the full path)
            int lastIndex = originalFilename.lastIndexOf('/');
            if (lastIndex == -1) {
                lastIndex = originalFilename.lastIndexOf('\\');
            }

            if (lastIndex != -1) {
                return originalFilename.substring(lastIndex + 1);
            }

            return originalFilename;
        }

        return null;
    }
}