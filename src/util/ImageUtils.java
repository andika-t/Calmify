package util;

import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImageUtils {

    public static String encodeAndResizeImageToBase64(File imageFile, int targetSize) {
        if (imageFile == null || !imageFile.exists()) {
            return null;
        }
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int newWidth, newHeight;

            if (originalWidth > originalHeight) {
                newWidth = targetSize;
                newHeight = (targetSize * originalHeight) / originalWidth;
            } else {
                newHeight = targetSize;
                newWidth = (targetSize * originalWidth) / originalHeight;
            }
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            System.err.println("Gagal membaca atau me-resize file gambar: " + e.getMessage());
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