package game.screen;

import game.Fonts;
import game.Images;
import game.SoundEngine;
import game.physics.Vector;
import server.Server;
import game.Window;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.event.KeyEvent;

public class TitleScreen implements Screen {
    private static String[] options = new String[] {
        "Local Game",
        "Host Game",
        "Join Game",
        "Settings",
        "Quit"
    };

    private int selector;

    public TitleScreen() {
        selector = 0;
    }

	@Override
	public Screen keyTyped(KeyEvent event) {
		return this;
	}

	@Override
	public Screen keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (selector) {
                case 0:
                    // Local game
                    return Window.preferredSelectScreen;
                case 1:
                    new Thread() {
                        @Override
                        public void run() {
                            Server.main(null);
                        }
                    }.start();
                    break;
                case 2:
                    SoundEngine.playSound("blip");
                    return new JoinGameScreen();
                case 3:
                    SoundEngine.playSound("blip");
                    return new SettingsScreen();
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("WARNING: Invalid title screen option selected");
                    System.exit(1);
                    break;
            }
        } else if (event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_S) {
            if (++selector > 4) selector = 0;
            SoundEngine.playSound("blip");
        } else if (event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_W) {
            if (--selector < 0) selector = 4;
            SoundEngine.playSound("blip");
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

    private int drawText(Vector pos, float size, String text, Color color, Graphics g, boolean lockedCenter) {
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
            y = rect.y + metrics.getAscent();
        }
        g2d.drawString(text, x, y);

        return metrics.stringWidth(text);
    }

	@Override
	public void render(Graphics g, ImageObserver target) {
		g.drawImage(Images.getImage("title_screen.png"), 0, 0, target);

        for (int i = 0; i < options.length; i++) {
            int textLength = drawText(new Vector(10, 10 + i * 50), 30, options[i], Color.WHITE, g, false);
            if (selector == i) g.fillRect(10 + textLength, 25 + i * 50, 30, 10);
        }
	}
}