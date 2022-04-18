package game;

import java.io.File;

import java.awt.Font;
import java.io.InputStream;

public class Fonts {
    private static Font SAB_FONT;

    public static Font getSABFont() {
        return SAB_FONT;
    }

    static {
        try {
            File fontFile = new File("assets/fonts/SAB_font.ttf");
            SAB_FONT = Font.createFont(0, fontFile);
        } catch (Exception e) {
        }

        try {
            InputStream in = Fonts.class.getClass().getResourceAsStream("/assets/fonts/SAB_font.ttf");
            SAB_FONT = Font.createFont(0, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}