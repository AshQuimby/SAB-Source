package game.screen;

import java.io.Serializable;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.event.KeyEvent;

public interface Screen extends Serializable {
    Screen keyTyped(KeyEvent event);

    Screen keyPressed(KeyEvent event);

    Screen keyReleased(KeyEvent event);

    Screen update();

    void render(Graphics g, ImageObserver target);
}