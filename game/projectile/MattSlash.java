package game.projectile;

import game.Player;
import game.physics.*;
import game.SoundEngine;

public class MattSlash extends Projectile {
    public MattSlash(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 12;
        width = 52;
        this.direction = direction;
        height = 64;
        life = 6;
        alive = true;
        knockbackStrength = 12;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        if (ownerPlayer.costume > 0)
            fileName = "matt_slash_alt_" + ownerPlayer.costume + ".png";
        else
            fileName = "matt_slash.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        velocity.x += 8 * ownerPlayer.direction;
        if (life < 6) {
            frame++;
        }
        if (--life == 0) {
            alive = false;
        }
        if (frame == 3) {
            SoundEngine.playSound("chomp");
        }
        pos.x = ownerPlayer.center().x - width / 2 + (24 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 32;
        move(velocity, false);
        hitbox.setPosition(pos);
        if (life > 2) 
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