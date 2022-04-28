package game.projectile;

import game.Player;
import game.physics.*;
import game.SoundEngine;

public class MegaSlash extends BufferedProjectile {
    public MegaSlash(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
            Player ownerPlayer, int damageTaken) {
        damage = damageTaken * 2;
        width = 128;
        this.direction = direction;
        height = 128;
        life = 24;
        alive = true;
        bufferTicks = 14;
        knockbackStrength = 50 * (1 + damageTaken * 2 * 0.01);
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        if (ownerPlayer.costume > 0)
            fileName = "mega_slash_alt_" + ownerPlayer.costume + ".png";
        else
            fileName = "mega_slash.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        draw = false;
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }
    
    @Override
    public void updateWhileBuffering() {
      ownerPlayer.invincible = true;
      ownerPlayer.velocity = ownerPlayer.velocity.mul(0);
      ownerPlayer.frame = 4;
      ownerPlayer.frameTimer = 0;
    }
    
    @Override
    public void onUnbuffer() {
        draw = true;
        ownerPlayer.frame = 5;
    }
    
    @Override
    public void updatePostBuffer() {
        ownerPlayer.invincible = true;
        ownerPlayer.velocity = ownerPlayer.velocity.mul(0);
        if (life % 4 == 0) {
            frame++;
        }
        if (--life == 0) {
            alive = false;
        }
        pos.x = ownerPlayer.center().x - width / 2 + (48 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 84;
        move(velocity, false);
        hitbox.setPosition(pos);
        if (frame == 2) 
           hittingPlayer();
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer(player, 1);
        SoundEngine.playSound("chomp");
    }

    @Override
    public void kill() {
    } // Unused
}