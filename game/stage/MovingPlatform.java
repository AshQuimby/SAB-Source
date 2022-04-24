package game.stage;

import game.Player;
import game.projectile.Projectile;
import game.physics.Vector;

public class MovingPlatform extends UpdatingPlatform {
   
    public Stage stage;
    public Vector velocity;
   
    public MovingPlatform(int x, int y, int width, int height, boolean canJumpThrough, String image, Stage stage) {
        super(x, y, width, height, canJumpThrough, image);
        this.stage = stage;
        velocity = new Vector(0, 0);
    }

    @Override
    public final void update() {
        hitbox.x += velocity.x;
        hitbox.y += velocity.y;

        for (Player player : stage.battleScreen.getPlayerList()) {
            if (hitbox.overlaps(player.hitbox)) {
               stage.collideWithPlatformsY(player, -velocity.y);
               stage.collideWithPlatformsX(player, -velocity.x);
            }
        }
        for (Projectile projectile : stage.battleScreen.getProjectiles()) {
            if (hitbox.overlaps(projectile.hitbox) && projectile.fixCollideWithMovingPlatforms()) {
               projectile.hitbox.resolveX(-velocity.y, hitbox);
               projectile.hitbox.resolveY(-velocity.x, hitbox);
            }
        }
        postUpdate();
    }
    
    public void postUpdate() {
    }
}