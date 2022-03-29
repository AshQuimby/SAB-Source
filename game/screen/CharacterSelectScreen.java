package game.screen;

import game.character.Character;
import game.character.*;
import game.SoundEngine;
import game.Images;
import game.physics.Vector;
import game.Fonts;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.ArrayList;

import modloader.ModReader;

import java.awt.image.ImageObserver;
import java.awt.event.KeyEvent;

public class CharacterSelectScreen implements Screen {
    public static ArrayList<Character> characters = new ArrayList<>();
    public static ArrayList<String> characterRenders = new ArrayList<>();

    private int player1CharacterIndex;
    private int player2CharacterIndex;

    private int player1CostumeIndex;
    private int player2CostumeIndex;

    private boolean player1Ready;
    private boolean player2Ready;

    static {
        characters.add(new Marvin());
        characters.add(new Chain());
        characters.add(new Walouis());
        characters.add(new Gus());
        characters.add(new EmperorEvil());
        characters.add(new BigSeagull());
        characters.add(new Snas());
        characters.add(new Stephane());
        characters.add(new Duck());
        characters.add(new Matthew());
        characters.add(new EmptySoldier());
        characters.add(new John());
    }

    public static void loadModCharacters() {
        characters.addAll(ModReader.getModCharacters());
        for (Character character : characters) {
            characterRenders.add(
                    character.fileName.substring(0, character.fileName.length() - 4) + "_render");
        }
    }

    public CharacterSelectScreen() {
        init();

        player1CharacterIndex = 0;
        player2CharacterIndex = 1;
    }

    public void init() {
        player1Ready = false;
        player2Ready = false;
        SoundEngine.playMusic("lobby_music");
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

    private void drawText(Vector pos, float size, String[] text, Color color, Graphics g, boolean lockedCenter) {
        for (int i = 0; i < text.length; i++) {
            drawText(new Vector(pos.x, pos.y + (size + 4) * i), size, text[i], color, g, lockedCenter);
        }
    }

    @Override
    public Screen keyTyped(KeyEvent event) {
        return this;
    }

    @Override
    public Screen keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_RIGHT && !player2Ready) {
            SoundEngine.playSound("blip");

            player2CharacterIndex++;
            if (player2CharacterIndex >= characters.size()) {
                player2CharacterIndex = 0;
            }
        }

        if (keyCode == KeyEvent.VK_LEFT && !player2Ready) {
            SoundEngine.playSound("blip");

            player2CharacterIndex--;
            if (player2CharacterIndex < 0) {
                player2CharacterIndex = characters.size() - 1;
            }
        }

        if (keyCode == KeyEvent.VK_UP) {
            SoundEngine.playSound("blip");

            player2CostumeIndex++;
            if (player2CostumeIndex > characters.get(player2CharacterIndex).altCount) {
                player2CostumeIndex = 0;
            }
        }

        if (keyCode == KeyEvent.VK_DOWN) {
            SoundEngine.playSound("blip");

            player2CostumeIndex--;
            if (player2CostumeIndex < 0) {
                player2CostumeIndex = characters.get(player2CharacterIndex).altCount;
            }
        }

        if (keyCode == KeyEvent.VK_W) {
            SoundEngine.playSound("blip");

            player1CostumeIndex++;
            if (player1CostumeIndex > characters.get(player1CharacterIndex).altCount) {
                player1CostumeIndex = 0;
            }
        }

        if (keyCode == KeyEvent.VK_S) {
            SoundEngine.playSound("blip");

            player1CostumeIndex--;
            if (player1CostumeIndex < 0) {
                player1CostumeIndex = characters.get(player1CharacterIndex).altCount;
            }
        }

        if (keyCode == KeyEvent.VK_M) {
            player2Ready = !player2Ready;

            if (player2Ready)
                SoundEngine.playSound("ready");
            else
                SoundEngine.playSound("unready");
        }

        if (keyCode == KeyEvent.VK_D && !player1Ready) {
            SoundEngine.playSound("blip");

            player1CharacterIndex++;
            if (player1CharacterIndex >= characters.size()) {
                player1CharacterIndex = 0;
            }
        }

        if (keyCode == KeyEvent.VK_A && !player1Ready) {
            SoundEngine.playSound("blip");

            player1CharacterIndex--;
            if (player1CharacterIndex < 0) {
                player1CharacterIndex = characters.size() - 1;
            }
        }

        if (keyCode == KeyEvent.VK_F || keyCode == KeyEvent.VK_Q) {
            player1Ready = !player1Ready;

            if (player1Ready)
                SoundEngine.playSound("ready");
            else
                SoundEngine.playSound("unready");
        }

        if (keyCode == KeyEvent.VK_ENTER && player1Ready && player2Ready) {
            SoundEngine.playSound("ready");
            return new StageSelectScreen(characters.get(player1CharacterIndex).copy(),
                    characters.get(player2CharacterIndex).copy(),
                    player1CostumeIndex, player2CostumeIndex);
        }

        if (keyCode == KeyEvent.VK_SHIFT) {
            SoundEngine.playSound("blip");
            return new SettingsScreen();
        }

        return this;
    }

    @Override
    public Screen keyReleased(KeyEvent event) {
        return this;
    }

    @Override
    public Screen update() {
        if (player1CostumeIndex > characters.get(player1CharacterIndex).altCount) {
            player1CostumeIndex = characters.get(player1CharacterIndex).altCount;
        }
        if (player2CostumeIndex > characters.get(player2CharacterIndex).altCount) {
            player2CostumeIndex = characters.get(player2CharacterIndex).altCount;
        }
        return this;
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        g.drawImage(Images.getImage("character_selector_background.png"), 0, 0, target);

        BufferedImage player1Selector = Images.getImage(player1Ready ? "p1selector.png" : "p1selector_gray.png");
        BufferedImage player2Selector = Images.getImage(player2Ready ? "p2selector.png" : "p2selector_gray.png");

        if (player1CostumeIndex != 0) {
            g.drawImage(
                    Images.getImage(
                            characterRenders.get(player1CharacterIndex) + "_alt_" + player1CostumeIndex + ".png"),
                    128, 300, 128, 128,
                    target);
        } else {
            g.drawImage(Images.getImage(characterRenders.get(player1CharacterIndex) + ".png"), 128, 300, 128, 128,
                    target);
        }
        if (player2CostumeIndex != 0) {
            g.drawImage(
                    Images.getImage(
                            characterRenders.get(player2CharacterIndex) + "_alt_" + player2CostumeIndex + ".png"),
                    1152 - 256, 300, 128,
                    128,
                    target);
        } else {
            g.drawImage(Images.getImage(characterRenders.get(player2CharacterIndex) + ".png"), 1152 - 256, 300, 128,
                    128, target);
        }
        g.drawImage(player1Selector, 128, 300, target);
        g.drawImage(player2Selector, 1152 - 256, 300, target);
        drawText(new Vector(576 - 400, 256), 16, characters.get(player1CharacterIndex).characterName,
                new Color(255, 255, 255), g, true);
        drawText(new Vector(576 + 400, 256), 16, characters.get(player2CharacterIndex).characterName,
                new Color(255, 255, 255), g, true);

        drawText(new Vector(576 - 300, 512), 8.5f, characters.get(player1CharacterIndex).description,
                new Color(255, 255, 255), g, true);
        drawText(new Vector(576 + 300, 512), 8.5f, characters.get(player2CharacterIndex).description,
                new Color(255, 255, 255), g, true);
    }
}