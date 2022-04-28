package game.projectile;

import game.Player;
import game.physics.*;

public class AngrySoul extends Projectile {

    public AngrySoul(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 16;
        width = 64;
        height = 24;
        this.direction = direction;
        alive = true;
        life = 40;
        knockbackStrength = 12;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "angry_soul.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        hitbox = new AABB(pos.x, pos.y, width, height);
        unreflectable = false;
    }

    @Override
    public void update() {
        velocity.y = Math.sin(life * Math.PI * 2 * .1) * 3;
        move(velocity, true);
        if (++frame >= 4) {
            frame = 0;
        }
        hittingPlayer();
        if (--life == 0) {
            alive = false;
        }
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer(player, 7);
    }

    @Override
    public void kill() {
    }
}