package game;

import java.util.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class Images {
    private static final Map<String, BufferedImage> images = new HashMap<>();

    static {
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
        if (!images.containsKey(filePath)) {
            System.out.println(String.format("WARNING: Invalid image requested: %s", filePath));
        }

        return images.get(filePath);
    }
}