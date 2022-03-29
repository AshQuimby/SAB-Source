package game.projectile;

import game.Player;
import game.physics.*;

public class UpwardsSlash extends Projectile {
    public UpwardsSlash(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 14;
        width = 64;
        this.direction = direction;
        height = 96;
        life = 50;
        alive = true;
        knockbackStrength = 28;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        if (ownerPlayer.costume > 0)
            fileName = "upwards_slash_alt_" + ownerPlayer.costume + ".png";
        else
            fileName = "upwards_slash.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
        updatesPerTick = 5;
    }

    @Override
    public void update() {
        if (--life == 0) {
            alive = false;
        }
        if (life % 10 == 0) {
            frame++;
        }
        ownerPlayer.velocity.y = -life;
        ownerPlayer.velocity.x *= 0.5;
        ownerPlayer.frame = 9;
        ownerPlayer.touchingStage = false;
        pos.x = ownerPlayer.center().x - width / 2;
        pos.y = ownerPlayer.center().y - height / 2 - 24;
        hittingPlayer();
        hitbox.setPosition(pos);
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 1;
    }

    @Override
    public boolean drawPriority() {
        return true;
    }

    @Override
    public void kill() {
    } // Unused
}