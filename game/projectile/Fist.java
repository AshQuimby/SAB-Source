package game.projectile;

import game.Player;
import game.physics.*;
import game.SoundEngine;

public class Fist extends BufferedProjectile {
    public Fist(double x, double y, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 22;
        width = 24;
        this.direction = direction;
        height = 52;
        life = 8;
        bufferTicks = 8;
        alive = true;
        knockbackStrength = 32;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        draw = false;
        fileName = "fist.png";
        pos = new Vector(x, y);
        velocity = new Vector(0, 0);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void updateWhileBuffering() {
        pos.x = ownerPlayer.center().x - width / 2 + (14 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 44;
    }

    @Override
    public void onUnbuffer() {
        draw = true;
    }

    @Override
    public void updatePostBuffer() {
        if (--life == 0) {
            alive = false;
        }
        if (life % 2 == 0) {
            frame++;
        }
        if (frame == 0) {
            SoundEngine.playSound("crunch");
        }
        if (ownerPlayer.touchingStage) {
            damage = 22;
        } else {
            damage = 28;
        }
        pos.x = ownerPlayer.center().x - width / 2 + (48 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 24;
        pos.add(velocity);
        hitbox.setPosition(pos);
        hittingPlayer();
    }

    @Override
    public void onHitPlayer(Player player) {
        battleScreen.cameraShake(10);
        hitPlayer(player, 1);
    }

    @Override
    public void kill() {
    } // Unused
}