package util;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImageUtils {

    public static String encodeImageToBase64(File imageFile) {
        if (imageFile == null || !imageFile.exists()) {
            return null;
        }
        try {
            byte[] fileContent = Files.readAllBytes(imageFile.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            System.err.println("Gagal membaca file gambar: " + e.getMessage());
            return null;
        }
    }

    public static Image decodeBase64ToImage(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            return new Image(new ByteArrayInputStream(imageBytes));
        } catch (IllegalArgumentException e) {
            System.err.println("String Base64 tidak valid: " + e.getMessage());
            return null;
        }
    }
}