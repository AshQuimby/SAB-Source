package game.stage;

import game.physics.AABB;
import game.physics.Vector;
import game.projectile.Fireball;
import game.projectile.Frostball;

import java.util.Random;

import game.Player;

public class Wobbler extends UpdatingPlatform {

    private int life;
    double fakeWidth;
    double fakeHeight;
    Stage stage;
    Vector originPoint;
    Vector toPos;
    Vector velocity;

    public Wobbler(int x, int y, int width, int height, boolean canJumpThrough, String image, Stage stage) {
        super(x, y, width, height, canJumpThrough, image);
        fakeWidth = 0;
        fakeHeight = 0;
        velocity = new Vector(0, 0);
        life = 0;
        this.stage = stage;
        originPoint = new Vector(x + 32, y + 32);
        toPos = originPoint;
    }

    @Override
    public void update() {
        life++;
        if (Vector.sub(toPos, originPoint).len() < 32 || life % 120 == 0) {
            toPos = stage.battleScreen.getPlayerList()
                    .get(new Random().nextInt(stage.battleScreen.getPlayerList().size())).hitbox.getCenter();
        } else {
            velocity = velocity.mul(0.9);
            velocity = Vector.add(velocity, Vector.normalize(Vector.sub(toPos, originPoint).mul(8)));
        }
        for (Player player : stage.battleScreen.getPlayerList()) {
            if (player.hitbox.overlaps(new AABB(hitbox.x - 16, hitbox.y - 16, hitbox.width + 32, hitbox.height + 32)))
                player.hitPlayerByNonProjectile(5, 10, velocity.rotationOf(), 0.02);
        }
        if (Math.random() > 0.9) {
            stage.battleScreen.addProjectileAtCenter(
                    new Fireball(Math.random() * stage.safeBlastZone.width + stage.safeBlastZone.x, -800, -12,
                            32, 12, Math.toRadians(110), -1, null));
            stage.battleScreen.addProjectileAtCenter(
                    new Frostball(Math.random() * stage.safeBlastZone.width + stage.safeBlastZone.x, -800, -12,
                            32, 12, Math.toRadians(110), 30, -1, null));
        }
        originPoint.add(velocity);
        hitbox.setCenter(originPoint);
        fakeWidth += Math.cos(life / 2) * 80;
        fakeHeight += Math.sin(life / 2) * 80;
        hitbox.width = Math.abs(fakeWidth) + 20;
        hitbox.height = Math.abs(fakeHeight) + 20;
    }
}