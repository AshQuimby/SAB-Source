package game.projectile;

import game.Player;
import game.physics.*;

public class Quack extends Projectile {
    public Quack(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        this.direction = direction;
        damage = 16;
        width = 44;
        height = 52;
        life = 10;
        alive = true;
        knockbackStrength = 12;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "quack.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        alive = --life != 0;
        if ((life + 1) % 2 == 0) {
            frame++;
        }
        pos.x = ownerPlayer.center().x - width / 2 + (36 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 42;
        pos.add(velocity);
        hitbox.setPosition(pos);
        hittingPlayer();
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer(player, 1);
    }

    @Override
    public void kill() {
    } // Unused
}