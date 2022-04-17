package game.projectile.final_asses;

import game.Player;
import game.physics.*;
import game.SoundEngine;
import game.projectile.Projectile;

public class JohnBall extends Projectile {
    public JohnBall(Player ownerPlayer) {
        damage = 16;
        width = 128;
        this.direction = ownerPlayer.direction;
        height = 128;
        life = 240;
        alive = true;
        knockbackStrength = 46;
        this.owner = ownerPlayer.keyLayout;
        this.ownerPlayer = ownerPlayer;
        dir = 0;
        fileName = "john_ball.png";
        pos = new Vector(ownerPlayer.hitbox.x, ownerPlayer.hitbox.y);
        velocity = new Vector(0, 8);
        unreflectable = true;
        speed = 1;
        unParryable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
        hitbox.setCenter(ownerPlayer.center());
        pos = hitbox.getPosition();
    }

    int speed;

    @Override
    public void update() {
        if (life == 240) {
            hitbox.y -= 48;
            velocity = new Vector(0, 8);
            move(velocity, true);
        }
        if (--life == 0) {
            alive = false;
        }
        ownerPlayer.invincible = true;
        ownerPlayer.velocity = velocity;
        if (++frame >= 4) {
            SoundEngine.playSound("swish");
            frame = 0;
        }

        ownerPlayer.hitbox.setCenter(center());
        ownerPlayer.render = false;

        ownerPlayer.stuck = 1;

        if (ownerPlayer.readableKeys[Player.RIGHT]) {
            direction = 1;
            velocity.x += 1;
        }

        if (ownerPlayer.readableKeys[Player.LEFT]) {
            direction = -1;
            velocity.x -= 1;
        }

        if (ownerPlayer.readableKeysJustPressed[Player.JUMP] == 1 || ownerPlayer.readableKeysJustPressed[Player.UP] == 1) {
            velocity.y = -42;
        }

        boolean onGround = move(velocity, true);

        if (onGround) {
            velocity.x *= 0.9;
            velocity.y = -32;
            battleScreen.cameraShake(20);
            SoundEngine.playSound("crash");
        } else {
            velocity.x *= 0.95;
        }

        velocity.x += 2 * direction;

        velocity.y += 4;

        pos = hitbox.getPosition();

        dir = direction == 1 ? Math.toRadians(-30) : Math.toRadians(210);
        hittingPlayer();
        hitPlayer--;
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 12;
        SoundEngine.playSound("chomp");
    }

    @Override
    public void kill() {
        ownerPlayer.render = true;
    }
}