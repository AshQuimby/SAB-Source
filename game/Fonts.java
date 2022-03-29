package game;

import java.io.File;

import java.awt.Font;
import java.io.IOException;
import java.awt.FontFormatException;

public class Fonts {
    private static Font SAB_FONT;

    public static Font getSABFont() {
        return SAB_FONT;
    }

    static {
        try {
            File fontFile = new File("assets/fonts/SAB_font.ttf");
            SAB_FONT = Font.createFont(0, fontFile);
        } catch (FontFormatException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}