package game.projectile;

import game.Player;
import game.physics.*;

public class ViceroyWings extends Projectile {

    public ViceroyWings(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 2;
        width = 128;
        height = 128;
        this.direction = -direction;
        alive = true;
        life = 10;
        knockbackStrength = 3;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "viceroy_wings.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        hitbox = new AABB(pos.x, pos.y, width, height);
        hitPlayer = 0;
        unreflectable = true;
    }

    @Override
    public void update() {
        move(velocity, false);

        if (life % 3 == 0) {
            if (++frame >= 5) {
                frame = 0;
            }
        }

        hittingPlayer();
        if (--life == 0) {
            alive = false;
        }
        hitbox.setCenter(ownerPlayer.center());
        pos = hitbox.getPosition();
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 7;
    }

    @Override
    public void kill() {
    }
}