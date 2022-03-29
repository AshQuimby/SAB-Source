package game.projectile;

import game.Player;
import game.physics.*;
import game.SoundEngine;

public class Peck extends Projectile {
    public Peck(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 20;
        width = 48;
        this.direction = direction;
        height = 36;
        life = 8;
        alive = true;
        knockbackStrength = 24;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "peck.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        if (--life == -2) {
            alive = false;
        }
        if (life < 2 && life > 0) {
            frame++;
        }
        if (frame == 3) {
            SoundEngine.playSound("chomp");
        }
        if (ownerPlayer.touchingStage) {
            damage = 20;
        } else {
            damage = 30;
        }
        pos.x = ownerPlayer.center().x - width / 2 + (64 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 32;
        pos.add(velocity);
        hitbox.setPosition(pos);
        hittingPlayer();
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 1;
    }

    @Override
    public void kill() {
    } // Unused
}