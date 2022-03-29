package game.projectile;

import game.Player;
import game.physics.*;

public class SwordSpike extends Projectile {
    public SwordSpike(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 14;
        width = 12;
        this.direction = direction;
        height = 52;
        life = 8;
        alive = true;
        knockbackStrength = 28;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        if (ownerPlayer.costume > 0)
            fileName = "sword_spike_alt_" + ownerPlayer.costume + ".png";
        else
            fileName = "sword_spike.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        if (--life == 0) {
            alive = false;
        }
        ownerPlayer.frame = 9;
        pos.x = ownerPlayer.center().x - width / 2;
        pos.y = ownerPlayer.center().y - 10;
        hittingPlayer();
        velocity.y += 12;
        move(velocity, true);
        hitbox.setPosition(pos);
    }

    @Override
    public void onHitPlayer(Player player) {
        ownerPlayer.velocity.y = -32;
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