package game;

import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import javax.imageio.ImageIO;
import java.io.File;

public class Images {
    private static final Map<String, BufferedImage> images = new HashMap<>();

    public static void load() {
        File assets = new File("assets/images");
        Queue<File> queue = new LinkedList<>();
        queue.offer(assets);

        while (!queue.isEmpty()) {
            File file = queue.poll();

            if (file.isDirectory()) {
                String[] fileNames = file.list();

                for (String fileName : fileNames) {
                    queue.offer(new File(fileName));
                }
            } else if (file.getName().endsWith(".png")) {
                BufferedImage image = loadImage(String.format("assets/images/%s", file));
                images.put(file.getName(), image);
            }
        }

        readModImages(new File("mods"));
    }

    private static void readModImages(File folder) {
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".png")) {
                BufferedImage image = loadImage(String.format(file.getPath()));
                images.put(file.getName(), image);
            } else if (file.isDirectory()) {
                readModImages(file);
            }
        }
    }

    public static BufferedImage alphaEffect(BufferedImage image, int alpha) {
        BufferedImage alphaImage = createCopy(image);
        for (int i = 0; i < alphaImage.getWidth(); i++) {
            for (int j = 0; j < alphaImage.getHeight(); j++) {
                Color newColor = new Color(alphaImage.getRGB(i, j), true);
                if (newColor.getAlpha() > 0)
                    newColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), alpha);
                alphaImage.setRGB(i, j, newColor.getRGB());
            }
        }
        return alphaImage;
    }

    public static BufferedImage colorAverageEffect(BufferedImage image, Color color) {
        BufferedImage colorImage = createCopy(image);
        for (int i = 0; i < colorImage.getWidth(); i++) {
            for (int j = 0; j < colorImage.getHeight(); j++) {
                Color newColor = new Color(colorImage.getRGB(i, j), true);
                newColor = new Color((newColor.getRed() + color.getRed()) / 2,
                        (newColor.getGreen() + color.getGreen()) / 2, (newColor.getBlue() + color.getBlue()) / 2, newColor.getAlpha());
                colorImage.setRGB(i, j, newColor.getRGB());
            }
        }
        return colorImage;
    }

    public static BufferedImage colorEffect(BufferedImage image, Color color) {
        BufferedImage colorImage = createCopy(image);
        for (int i = 0; i < colorImage.getWidth(); i++) {
            for (int j = 0; j < colorImage.getHeight(); j++) {
                Color newColor = new Color(colorImage.getRGB(i, j), true);
                int brightness = (newColor.getRed() + newColor.getGreen() + newColor.getBlue()) / 3;
                newColor = new Color((int) (brightness * (color.getRed() / 255f)), (int) (brightness * (color.getGreen() / 255f)), (int) (brightness * (color.getBlue() / 255f)), newColor.getAlpha());
                colorImage.setRGB(i, j, newColor.getRGB());
            }
        }
        return colorImage;
    }

    public static BufferedImage fillColorEffect(BufferedImage image, Color color) {
        BufferedImage colorImage = createCopy(image);
        for (int i = 0; i < colorImage.getWidth(); i++) {
            for (int j = 0; j < colorImage.getHeight(); j++) {
                Color oldColor = new Color(colorImage.getRGB(i, j), true);
                colorImage.setRGB(i, j, new Color(color.getRed(), color.getGreen(), color.getBlue(), oldColor.getAlpha()).getRGB());
            }
        }
        return colorImage;
    }

    private static BufferedImage createCopy(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static BufferedImage loadImage(String filePath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.out.println(String.format("Error loading image %s", filePath));
            e.printStackTrace();
            System.exit(1);
        }

        return image;
    }

    public static BufferedImage getImage(String filePath) {
        if (images.containsKey(filePath)) {
            return images.get(filePath);
        }

        BufferedImage image = null;
        try {
            InputStream in = Images.class.getClass().getResourceAsStream("/assets/images/" + filePath);

            image = ImageIO.read(in);
            images.put(filePath, image);
        } catch (IOException e) {
            System.out.println(String.format("WARNING: Invalid image requested: %s", filePath));
            e.printStackTrace();
        }

        return image;
    }
}