package game.screen;

import game.SoundEngine;
import game.Images;
import game.physics.Utilities;
import game.physics.Vector;
import game.Fonts;
import game.Settings;

import java.awt.Graphics;
import java.awt.*;

import java.awt.image.ImageObserver;
import java.awt.event.KeyEvent;

public class SettingsScreen implements Screen {

    int settingIndex;

    public SettingsScreen() {
        settingIndex = 0;
    }

    private void drawText(Vector pos, float size, String text, Color color, Graphics g, boolean lockedCenter) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
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
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_ENTER) {
            SoundEngine.playSound("blip");
            Settings.writeToFile();
            SoundEngine.playMusic("lobby_music");

            return new TitleScreen();
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            updateSettings(false);
            SoundEngine.playSound("blip");
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            updateSettings(true);
            SoundEngine.playSound("blip");
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            SoundEngine.playSound("blip");
            if (settingIndex++ > 7) {
                settingIndex = 0;
            }
        }
        if (keyCode == KeyEvent.VK_UP) {
            SoundEngine.playSound("blip");
            if (settingIndex-- < 1) {
                settingIndex = 8;
            }
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

    public void updateSettings(boolean rightKey) {
        if (rightKey) {
            if (settingIndex == 0) {
                Settings.setMusic(true);
                SoundEngine.playMusic("lobby_music");
            } else if (settingIndex == 1) {
                Settings.setSoundEffects(true);
            } else if (settingIndex == 2) {
                Settings.setFixedCamera(true);
            } else if (settingIndex == 3) {
                if (Settings.volume() < 0.99f)
                    Settings.setVolume(Settings.volume() + 0.05f);
            } else if (settingIndex == 4) {
                if (Settings.lives() < 5)
                    Settings.setLives(Settings.lives() + 1);
            } else if (settingIndex == 5) {
                Settings.setAIPlayer1(Utilities.overflow(Settings.aiPlayer1() + 1, 3, 0));
            } else if (settingIndex == 6) {
                Settings.setAIPlayer2(Utilities.overflow(Settings.aiPlayer2() + 1, 3, 0));
            } else if (settingIndex == 7) {
                Settings.setAssBalls(true);
            } else if (settingIndex == 8) {
                Settings.setStageHazards(true);
            }
        } else {
            if (settingIndex == 0) {
                Settings.setMusic(false);
                SoundEngine.stopMusic();
            } else if (settingIndex == 1) {
                Settings.setSoundEffects(false);
            } else if (settingIndex == 2) {
                Settings.setFixedCamera(false);
            } else if (settingIndex == 3) {
                if (Settings.volume() > 0.01f)
                    Settings.setVolume(Settings.volume() - 0.05f);
            } else if (settingIndex == 4) {
                if (Settings.lives() > 1)
                    Settings.setLives(Settings.lives() - 1);
            } else if (settingIndex == 5) {
                Settings.setAIPlayer1(Utilities.overflow(Settings.aiPlayer1() - 1, 3, 0));
            } else if (settingIndex == 6) {
                Settings.setAIPlayer2(Utilities.overflow(Settings.aiPlayer2() - 1, 3, 0));
            } else if (settingIndex == 7) {
                Settings.setAssBalls(false);
            } else if (settingIndex == 8) {
                Settings.setStageHazards(false);
            }
        }
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        g.drawImage(Images.getImage("settings_background.png"), 0, 0, target);

        drawText(new Vector(576, 64), 32, "SETTINGS", new Color(255, 255, 255), g, true);
        drawText(new Vector(576, 128), 24, "Enter to save settings", new Color(255, 255, 255), g, true);

        g.drawImage(Images.getImage("setting_selector.png"), 576 - 256 - 64, 252 + 32 * settingIndex, target);

        drawText(new Vector(576 - 256, 256), 16, "Music: " + (Settings.music() ? "On" : "Off"),
                new Color(255, 255, 255), g, false);
        drawText(new Vector(576 - 256, 256 + 32), 16, "Sound Effects: " + (Settings.soundEffects() ? "On" : "Off"),
                new Color(255, 255, 255), g, false);
        drawText(new Vector(576 - 256, 256 + 32 * 2), 16, "Fixed Camera: " + (Settings.fixedCamera() ? "On" : "Off"),
                new Color(255, 255, 255), g, false);
        drawText(new Vector(576 - 256, 256 + 32 * 3), 16, "Volume: " + Math.round(Settings.volume() * 100) + "%",
                new Color(255, 255, 255), g, false);
        drawText(new Vector(576 - 256, 256 + 32 * 4), 16, "Lives: " + Settings.lives(), new Color(255, 255, 255), g,
                false);
        drawText(new Vector(576 - 256, 256 + 32 * 5), 16,
                "Player 1: "
                        + (Settings.aiPlayer1() == 0 ? "Human" : Settings.aiPlayer1() == 1 ? "Easy AI" : Settings.aiPlayer1() == 2 ? "Good AI" : "Best AI"),
                new Color(255, 255, 255), g,
                false);
        drawText(new Vector(576 - 256, 256 + 32 * 6), 16,
                "Player 2: "
                        + (Settings.aiPlayer2() == 0 ? "Human" : Settings.aiPlayer2() == 1 ? "Easy AI" : Settings.aiPlayer2() == 2 ? "Good AI" : "Best AI"),
                new Color(255, 255, 255), g,
                false);
        drawText(new Vector(576 - 256, 256 + 32 * 7), 16, "Spawn Ass Balls: " + (Settings.assBalls() ? "On" : "Off"),
                new Color(255, 255, 255), g, false);
        drawText(new Vector(576 - 256, 256 + 32 * 8), 16, "Stage Hazards: " + (Settings.stageHazards() ? "On" : "Off"),
                new Color(255, 255, 255), g, false);
    }
}