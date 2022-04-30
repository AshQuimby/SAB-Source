package game.screen;

import game.SoundEngine;
import game.Images;
import game.physics.Vector;
import game.Fonts;

import java.awt.Graphics;
import java.awt.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import java.awt.image.ImageObserver;
import java.awt.event.KeyEvent;

public class CreditsScreen implements Screen {
    private Vector textWallPos = new Vector (1152 / 2, 760);
    
    public CreditsScreen() {
        SoundEngine.playMusicOnce("big_seagull_victory");
    }

    private float drawText(Vector pos, float size, String text, Color color, Graphics g, boolean lockedCenter) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(color);
        g2d.setFont(Fonts.getSABFont());
        g2d.setFont(g2d.getFont().deriveFont(size));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle((int) pos.x, (int) pos.y, metrics.stringWidth(text), (int) metrics.getHeight());
        int x;
        int y;
        if (lockedCenter) {
            x = rect.x - rect.width / 2;
            y = rect.y + rect.height / 2;
        } else {
            x = rect.x - (text.length() - 2) * 24;
            y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        }
        g2d.drawString(text, x, y);
        return rect.height;
    }

    @Override
    public Screen keyTyped(KeyEvent event) {
        return this;
    }

    @Override
    public Screen keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_ENTER) {
            SoundEngine.playSound("blip");
            return new TitleScreen();
        }
        return this;
    }

    @Override
    public Screen keyReleased(KeyEvent event) {
        return this;
    }

    @Override
    public Screen update() {
        return this;
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        textWallPos.y -= 1;
        if (textWallPos.y > 0) g.drawImage(Images.getImage("credit_background1.png"), 0, 0, 1152, 704, target);
        if (textWallPos.y < 255) g.drawImage(Images.alphaEffect(Images.getImage("credit_background2.png"), (int) Math.max(0, Math.min(255 - textWallPos.y * 2, 255))), 0, 0, 1152, 704, target);

        Scanner scanner = null;
        File f = new File("assets/credits.txt");
        try {
            scanner = new Scanner(f);
        } catch (FileNotFoundException e) {
        }
        
        float prevHeight = 0f;
        
        while (scanner.hasNext()) {
            String creditsLine = "";
            
            int size = scanner.nextInt();
                        
            while (!scanner.hasNext("#")) {
               if (scanner.hasNext()) {
                  creditsLine += scanner.next();
                  creditsLine += " ";
               }
            }
            scanner.next();
            prevHeight += drawText(Vector.add(textWallPos, new Vector(0, prevHeight)), size, creditsLine, new Color(255, 255, 255), g, true);
        }
        if (scanner != null)
            scanner.close();
    }
}