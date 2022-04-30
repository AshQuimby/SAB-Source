package game.stage;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.Serializable;

import game.physics.AABB;
import game.Images;
import game.screen.BattleScreen;

public class Platform implements Serializable {
    protected String image;
    protected AABB hitbox;
    protected boolean canJumpThrough;

    public Platform(int x, int y, int width, int height, boolean canJumpThrough, String image) {
        hitbox = new AABB(x, y, width, height);
        this.canJumpThrough = canJumpThrough;
        this.image = image;
    }

    public boolean canJumpThrough() {
        return canJumpThrough;
    }

    public AABB getHitbox() {
        return hitbox;
    }

    public String getImage() {
        return image;
    }

    public boolean updates() {
        return false;
    }
    
    public void render(BattleScreen battleScreen, Graphics g, ImageObserver target) {
         battleScreen.renderObject(g, Images.getImage(image), hitbox.getPosition(),
               (int) hitbox.width, (int) hitbox.height, true, target);
    }
}