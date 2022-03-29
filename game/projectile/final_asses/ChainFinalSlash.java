package game.projectile.final_asses;

import game.Player;
import game.physics.*;
import game.SoundEngine;
import game.projectile.Projectile;

public class ChainFinalSlash extends Projectile {
    public ChainFinalSlash(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 8;
        width = 256;
        this.direction = direction;
        height = 256;
        life = 80;
        alive = true;
        knockbackStrength = 56;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "chain_final_slash.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        unreflectable = true;
        speed = 1;
        unParryable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    int speed;

    @Override
    public void update() {
        ownerPlayer.hitbox.y -= 1;
        ownerPlayer.invincible = true;
        ownerPlayer.velocity = ownerPlayer.velocity.mul(0);
        if (life % (int) Math.max(4 - speed * 0.015, 1) == 0) {
            frame++;
        }
        if (frame > 12) {
            frame = 0;
        }
        if (--life == 0) {
            alive = false;
        }
        if (ownerPlayer.readableKeysJustPressed[Player.ATTACK] == 1) {
            speed++;
        }

        speed++;

        hitbox.setCenter(ownerPlayer.center());

        hitbox.x += 64 * ownerPlayer.direction;

        pos = hitbox.getPosition();

        dir = direction == 1 ? Math.toRadians(-30) : Math.toRadians(210);

        ownerPlayer.stunned = 1;
        ownerPlayer.frame = 6;
        if (frame == 2 || frame == 6 || frame == 10)
            hittingPlayer();
        else
            hitPlayer = 0;
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 1;
        player.hitbox.x = ((player.hitbox.x - player.hitbox.width / 2) * 3 + center().x) / 4;
        player.hitbox.y = ((player.hitbox.y - player.hitbox.height / 2) * 3 + center().y) / 4;
        if (player.stunned < 20)
            player.stunned = 20;
        if (life <= 8 && player.stunned > 4) {
            player.stunned = 4;
        }
        SoundEngine.playSound("chomp");
    }

    @Override
    public void kill() {
    } // Unused
}