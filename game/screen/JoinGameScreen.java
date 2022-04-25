package game.screen;

import game.Fonts;
import game.Images;
import game.physics.Vector;

import java.awt.*;
import java.awt.image.ImageObserver;

import client.Client;

import java.awt.event.KeyEvent;

public class JoinGameScreen implements Screen {
    String address;
    String port;
    int line;

    public JoinGameScreen() {
        address = "";
        port = "19128";
        line = 0;
    }

	@Override
	public Screen keyTyped(KeyEvent event) {
        return this;
	}

	@Override
	public Screen keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_DOWN) {
            if (++line > 1) line = 0;
        } else if (keyCode == KeyEvent.VK_UP) {
            if (--line < 0) line = 1;
        }

        if (line == 0 && Character.isLetterOrDigit(event.getKeyChar()) || event.getKeyChar() == '.') {
            address = address.concat(Character.toString(Character.toLowerCase(event.getKeyChar())));
        }

        if (line == 1 && Character.isDigit(event.getKeyChar())) {
            port = port.concat(Character.toString(Character.toLowerCase(event.getKeyChar())));
        }

        if (keyCode == KeyEvent.VK_BACK_SPACE) {
            if (line == 0 && address.length() > 0) address = address.substring(0, address.length() - 1);
            else if (line == 1 && port.length() > 0) port = port.substring(0, port.length() - 1);
        }

        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_SHIFT) {
            return new TitleScreen();
        }

        if (keyCode == KeyEvent.VK_ENTER) {
            new Thread() {
                @Override
                public void run() {
                    Client.main(new String[] {address, port});
                }
            }.start();
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
        g.drawImage(Images.getImage("loading_screen.png"), 0, 0, target);

        int addressWidth = drawText(new Vector(1152 / 2, 704 / 2), 30, "Address: " + address, Color.WHITE, g, true);
        int portWidth = drawText(new Vector(1152 / 2, 704 / 2 + 50), 30, "Port: " + port, Color.WHITE, g, true);

        g.setColor(Color.WHITE);
        if (line == 0) {
            g.fillRect(1152 / 2 + addressWidth / 2, 704 / 2, 10, 10);
        } else if (line == 1) {
            g.fillRect(1152 / 2 + portWidth / 2, 704 / 2 + 50, 10, 10);
        }
	}
    
}