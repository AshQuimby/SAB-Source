package mods.Frost_Elevator;

import modloader.ModProjectile;
import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class Slash extends ModProjectile {
    public Slash(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 15;
        width = 64;
        this.direction = direction;
        height = 128;
        life = 4;
        alive = true;
        knockbackStrength = 50;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "Slash.png";
        pos = new Vector(x, y);
        pos.x = ownerPlayer.center().x - width / 2 + (64 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 64;
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
        if (ownerPlayer.touchingStage) {
            knockbackStrength = 50;
            damage = 20;
        } else {
            knockbackStrength = 25;
            damage = 10;
        }
        pos.x = ownerPlayer.center().x - width / 2 + (64 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 64;
        pos.add(velocity);
        hitbox.setPosition(pos);
        hittingPlayer();
    }
    
    @Override
   public void onHitPlayer(Player player) {
      for (int i = 0; i < 4; i++) {
                battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
                        player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, "blood.png"));
            }
            SoundEngine.playSound("hitHurt");
            hitPlayer = 1;
   }

    @Override
    public void kill() {
    } // Unused
}