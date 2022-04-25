package game.screen;

import game.Images;
import game.SoundEngine;
import game.physics.Vector;
import game.Fonts;

import java.awt.Graphics;
import java.awt.*;

import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyEvent;

public class LoadingScreen implements Screen {

    List<BitEffect> bits = new ArrayList<>();
    List<BitEffect> deadBits = new ArrayList<>();

    public LoadingScreen() {
        SoundEngine.playMusic("loading");
    }

    private void drawText(Vector pos, float size, String text, Color color, Graphics g, boolean lockedCenter) {
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
            x = rect.x;
            y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        }
        g2d.drawString(text, x, y);
    }

    @Override
    public Screen keyTyped(KeyEvent event) {
        return this;
    }

    @Override
    public Screen keyPressed(KeyEvent event) {
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
        g.drawImage(Images.getImage("loading_screen.png"), 0, 0, target);
        drawText(new Vector(576, 320), 36, "LOADING", new Color(255, 0, 0), g,
                true);
        drawText(new Vector(576, 480), 12, "Waiting on other player to join", new Color(200, 50, 50), g,
                true);
        if (Math.random() > 0.5) {
            boolean upDown = Math.random() > 0.5;
            boolean negPos = Math.random() > 0.5;
            BitEffect bitBoy = new BitEffect(new Vector(1152 * Math.random(), 704 * Math.random()),
                    new Vector(upDown ? 4 : 0 * (negPos ? -1 : 1), upDown ? 0 : 4 * (negPos ? -1 : 1)));
            bitBoy.position = bitBoy.position.snap(16);
            bits.add(bitBoy);
        }
        for (BitEffect bit : bits) {
            bit.position.add(bit.velocity);
            if (Math.random() > 0.95) {
                deadBits.add(bit);
            }
        }
        if (bits.size() > 60) {
            deadBits.add(deadBits.get(0));
        }
        bits.removeAll(deadBits);
        deadBits.removeAll(deadBits);

        for (BitEffect bit : bits) {
            drawText(bit.position, 16, Math.random() > 0.5 ? "1" : "0", new Color(150, 25, 25), g, true);
        }
    }
}