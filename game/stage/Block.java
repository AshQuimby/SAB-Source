package game.stage;

import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.Serializable;

import game.particle.Particle;
import game.projectile.BlockSmash;
import game.projectile.Projectile;
import game.Images;
import game.screen.BattleScreen;

public class Block extends UpdatingPlatform {

    private int life;
    private Stage stage;

    public Block(int x, int y, int width, int height, boolean canJumpThrough, String image, Stage stage) {
        super(x, y, width, height, canJumpThrough, image);
        hitbox.x = (int) (Math.floor(hitbox.x / 32) * 32);
        hitbox.y = (int) (Math.floor(hitbox.y / 32) * 32);
        for (Platform platform : stage.platforms) {
            if (hitbox.overlaps(platform.hitbox)) {
                hitbox.y -= 32;
            }
        }
        for (int i = 0; i < 4; i++)
            stage.battleScreen.addParticle(
                    new Particle(hitbox.getCenter().x + 12, hitbox.getCenter().y + 12, (Math.random() - 0.5) * 3,
                            (Math.random() - 0.5) * 3, 1, 4, 4, "smoke.png"));
        this.stage = stage;
        life = 0;
    }

    @Override
    public void update() {
        for (Projectile projectile : stage.battleScreen.getProjectiles()) {
            if (hitbox.overlaps(projectile.hitbox)) {
                if (!projectile.unParryable
                        && projectile.getClass() != new BlockSmash(0, 0, 0, 0, 0, 0, 0, null).getClass()) {
                    if (!projectile.unreflectable) {
                        life += projectile.damage * 8;
                        projectile.alive = false;
                    } else {
                        life += projectile.damage;
                    }
                }
            }
        }
        if (life++ > 200) {
            for (int i = 0; i < 4; i++)
                stage.battleScreen.addParticle(
                        new Particle(hitbox.getCenter().x + 4, hitbox.getCenter().y + 4, (Math.random() - 0.5) * 3,
                                (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
            stage.deadPlatforms.add(this);
        }
    }
    
    @Override
    public void render(BattleScreen battleScreen, Graphics g, ImageObserver target) {
         battleScreen.renderObject(g, Images.getImage(image), hitbox.getPosition(),
               (int) hitbox.width, (int) hitbox.height, true, target);
         battleScreen.renderObject(g, Images.getImage("block_break.png"), hitbox.getPosition(),
               (int) hitbox.width, (int) hitbox.height, (int) (life / 40), true, target);
    }
}