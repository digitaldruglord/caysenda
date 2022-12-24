package com.nomi.caysenda.utils;



import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.AttributedString;

public class ImageUtils {
    public static BufferedImage compressImage(byte[] bytes) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        BufferedImage bufferedReturn = null;
        if (bufferedImage != null) {
            BufferedImage outputImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
            bufferedReturn = outputImage;
        }
        return bufferedReturn;
    }
    public static BufferedImage cropAndResize(byte[] bytes) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        BufferedImage bufferedReturn = null;
        if (bufferedImage != null) {
            int cropSize = 800;
            if (bufferedImage.getWidth() < bufferedImage.getHeight()) {
                cropSize = bufferedImage.getWidth();
            } else {
                cropSize = bufferedImage.getHeight();
            }
            int x = (bufferedImage.getWidth() - cropSize) / 2;
            int y = (bufferedImage.getHeight() - cropSize) / 2;
            bufferedImage = bufferedImage.getSubimage(x, y, cropSize, cropSize);
            Image resultingImage = bufferedImage.getScaledInstance(800, 800, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            bufferedReturn = outputImage;
        }
        return bufferedReturn;

    }
    public static BufferedImage cropAndResize(byte[] bytes,Integer size) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        BufferedImage bufferedReturn = null;
        if (bufferedImage != null) {
            Image resultingImage = bufferedImage.getScaledInstance(size, size, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            bufferedReturn = outputImage;
        }
        return bufferedReturn;

    }
    public static BufferedImage addText(String text,byte[] bytes){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
            Font font = new Font("Arial", Font.BOLD, 50);
            AttributedString attributedText = new AttributedString(text);
            attributedText.addAttribute(TextAttribute.FONT, font);
            attributedText.addAttribute(TextAttribute.FOREGROUND, Color.RED);
            Graphics g = image.getGraphics();
            FontMetrics metrics = g.getFontMetrics();
            int positionX = 20;
            int positionY = (image.getHeight() - metrics.getHeight()) + metrics.getAscent()-50;
            g.drawString(attributedText.getIterator(), positionX, positionY);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
