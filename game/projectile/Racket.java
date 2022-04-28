package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class Racket extends Projectile {
   public Racket(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 14;
      width = 208;
      height = 116;
      life = 12;
      alive = true;
      knockbackStrength = 0;
      this.direction = direction;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "tennis.png"; // hehehehehehehehheheheheh im hilarious trolled
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   int frameTimer = 0;
   int charge;

   @Override
   public void update() {
      if (ownerPlayer.tookDamage) {
         alive = false;
         ownerPlayer.charging = false;
      }
      ownerPlayer.velocity.y -= 2;
      if (ownerPlayer.charging) {
         charge = ownerPlayer.charge;
         life++;
         frame = 0;
      } else if (++frameTimer >= 2) {
         if (charge > 60)
            charge = 60;
         damage = charge / 2 + 18;
         knockbackStrength = charge * 1.6 + 16;
         if (++frame >= 7) {
            frame = 0;
         }
         frameTimer = 0;
      }
      if (!ownerPlayer.charging)
         ownerPlayer.endLag = 1;
      ownerPlayer.direction = direction;
      if (--life == 0) {
         alive = false;
      }
      ownerPlayer.frame = 9;
      if (!ownerPlayer.charging)
         ownerPlayer.frame = 3;
      if (frame == 2) {
         ownerPlayer.frame = 5;
         if (direction == -1) {
            dir = Math.toRadians(220);
         } else {
            dir = Math.toRadians(320);
         }
         for (Projectile projectile : ownerPlayer.battleScreen.getProjectiles()) {
            if (projectile.hitbox.overlaps(hitbox) && !projectile.unreflectable
                  && projectile.ownerPlayer != ownerPlayer) {
               projectile.owner = owner;
               projectile.ownerPlayer = ownerPlayer;
               projectile.velocity.x *= -1 - (charge / 30.0);
               projectile.damage *= charge / 10;
               projectile.knockbackStrength *= charge / 10;
               projectile.dir = projectile.velocity.rotationOf();
               for (int i = 0; i < 4; i++) {
                  battleScreen.addParticle(new Particle(projectile.pos.x + projectile.width / 2,
                        projectile.pos.y + projectile.height / 2, (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, "twinkle.png"));
               }
            }
         }
      } else if (frame == 3) {
         ownerPlayer.frame = 4;
         if (direction == -1) {
            dir = Math.toRadians(100);
         } else {
            dir = Math.toRadians(80);
         }
      }
      pos.x = ownerPlayer.center().x - 104;
      pos.y = ownerPlayer.hitbox.y - 28;
      hitbox.setPosition(pos);
      if (frame == 2 || frame == 3)
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