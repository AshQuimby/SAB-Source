package game.projectile;

import game.Player;
import game.physics.*;

public class DownPunch extends Projectile {
    public DownPunch(double x, double y, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 12;
        width = 52;
        this.direction = direction;
        height = 32;
        life = 4;
        alive = true;
        knockbackStrength = 18;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "down_punch.png";
        pos = new Vector(x, y);
        velocity = new Vector(0, 0);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        if (--life == 0) {
            alive = false;
        }
        ownerPlayer.frame = 13;
        frame++;
        pos.x = ownerPlayer.center().x - width / 2;
        pos.y = ownerPlayer.center().y + 40;
        hittingPlayer();
        move(velocity, false);
        hitbox.setPosition(pos);
    }

    @Override
    public void onHitPlayer(Player player) {
        ownerPlayer.velocity.y = -14;
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