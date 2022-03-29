package game.screen;

import game.SoundEngine;
import game.Images;
import game.physics.Vector;
import game.Fonts;
import game.Player;
import game.Window;

import java.awt.Graphics;
import java.awt.*;

import java.awt.image.ImageObserver;
import java.awt.event.KeyEvent;

public class EndScreen implements Screen {
    Player winner;
    int gameEndTimer;

    public EndScreen(Player winner) {
        this.winner = winner.lightClone();
        gameEndTimer = 8;
        SoundEngine.playMusicOnce(
                winner.selectedChar.fileName.substring(0, winner.selectedChar.fileName.length() - 4) + "_victory");
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
            x = rect.x - (text.length() - 2) * 24;
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
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_ENTER) {
            SoundEngine.playSound("blip");
            CharacterSelectScreen toScreen = (CharacterSelectScreen) Window.preferredSelectScreen;
            toScreen.init();
            return toScreen;
        }
        return this;
    }

    @Override
    public Screen keyReleased(KeyEvent event) {
        return this;
    }

    @Override
    public Screen update() {
        if (gameEndTimer > 0) {
            gameEndTimer--;
        }
        return this;
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        g.drawImage(Images.getImage("game_end_background.png"), 0, 0, target);

        if (winner.costume != 0) {
            g.drawImage(Images.getImage(
                    winner.selectedChar.fileName.substring(0, winner.selectedChar.fileName.length() - 4)
                            + "_render_alt_" + winner.costume + ".png"),
                    576 - 128 + gameEndTimer / 2, 300 - gameEndTimer * 64, 256 - gameEndTimer, 256, target);
        } else {
            g.drawImage(Images.getImage(
                    winner.selectedChar.fileName.substring(0, winner.selectedChar.fileName.length() - 4)
                            + "_render.png"),
                    576 - 128 + gameEndTimer / 2, 300 - gameEndTimer * 64, 256 - gameEndTimer, 256, target);
        }
        drawText(new Vector(576, 128), 36, winner.selectedChar.characterName + " WINS!", new Color(255, 255, 255), g,
                true);
    }
}