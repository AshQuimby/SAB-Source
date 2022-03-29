package mods.Tachyon;

import modloader.ModProjectile;
import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class Push extends ModProjectile {
    public Push(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 10;
        width = 64;
        this.direction = direction;
        height = 64;
        life = 5;
        alive = true;
        knockbackStrength = 50;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "Push_alt_" + ownerPlayer.costume + ".png";
        pos = new Vector(x, y);
        pos.x = ownerPlayer.center().x - width / 2 - (5 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y + 32;
        velocity = new Vector(velX, velY);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        if (--life == -2) {
            alive = false;
        }
        frame++;
        pos.x = ownerPlayer.center().x - width / 2 - (5 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y + 32;
        pos.add(velocity);
        hitbox.setPosition(pos);
        hittingPlayer();
    }
    
    @Override
   public void onHitPlayer(Player player) {
      hitPlayer = 1;
      SoundEngine.playSound("spike");
   }

    @Override
    public void kill() {
    } // Unused
}