package game.particle;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import game.Images;
import game.screen.BattleScreen;

public class Smoke extends AnimatedParticle {
    public Smoke(double x, double y, double velX, double velY, String fileName) {
        super(x, y, velX, velY, 1, 32, 32, fileName, 4, 6);
    }

    @Override
    public void render(Graphics g, BattleScreen battleScreen, ImageObserver target) {
        battleScreen.renderObject(g, Images.alphaEffect(Images.getImage(fileName), 255 - 16 * frame), pos, (int) (width * scale),
                (int) (height * scale), frame, false, target);
    }
}
