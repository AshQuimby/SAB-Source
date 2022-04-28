package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class Bite extends Projectile {
   public Bite(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 24;
      width = 76;
      this.direction = direction;
      height = 64;
      life = 8;
      alive = true;
      knockbackStrength = 12;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "bite.png";
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
      if (life < 5 && life > 0) {
         frame++;
      }
      if (frame == 3) {
         SoundEngine.playSound("chomp");
      }
      if (ownerPlayer.touchingStage) {
         damage = 24;
      } else {
         damage = 32;
      }
      pos.x = ownerPlayer.center().x - width / 2 + (64 * ownerPlayer.direction);
      pos.y = ownerPlayer.center().y - 54;
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
      hitPlayer(player, 1);
   }

   @Override
   public void kill() {
   } // Unused
}