package game.projectile;

import game.Player;
import game.physics.*;
import game.SoundEngine;

public class Teleport extends Projectile {
    public Teleport(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 24;
        width = 100;
        this.direction = direction;
        height = 80;
        life = 12;
        alive = true;
        knockbackStrength = 24;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        if (ownerPlayer.costume > 0)
            fileName = "matt_teleport_alt_" + ownerPlayer.costume + ".png";
        else
            fileName = "matt_teleport.png";
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        frame++;
        if (--life == 0) {
            alive = false;
        }
        if (life < 5) {
            ownerPlayer.render = true;
            ownerPlayer.invincible = true;
        } else {
            ownerPlayer.render = true;
        }
        pos.x = ownerPlayer.center().x - width / 2 - 8 * ownerPlayer.direction;
        pos.y = ownerPlayer.center().y - height / 2;
        move(velocity, false);
        hitbox.setPosition(pos);
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 1;
        SoundEngine.playSound("chomp");
    }

    @Override
    public void kill() {
    } // Unused
    
   @Override
   public boolean drawPriority() {
      return true;
   }
}