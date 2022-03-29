package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.*;

public class RevolverRound extends Projectile {
   public RevolverRound(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 38;
      width = 8;
      height = 8;
      life = 30;
      updatesPerTick = 5;
      alive = true;
      knockbackStrength = kb;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "bullet.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      battleScreen.addParticle(
            new Particle(pos.x + 12, pos.y + 12, velocity.x / 4, 0, 1, 4, 4, "smoke.png"));
      if (--life == 0) {
         alive = false;
      }
      boolean colliding = move(velocity, true);
      hitbox.setPosition(pos);
      if (colliding) {
         alive = false;
      }
      hitbox.setPosition(pos);
      if (life == 29) {
         battleScreen
               .addParticle(
                     new Particle(pos.x + width / 2 - 4, pos.y + height / 2 - 4, 0, 0, 2, 8, 8, "twinkle.png"));
         battleScreen
               .addParticle(new Particle(pos.x + width / 2 - 4, pos.y + height / 2 - 4, (Math.random() - 0.5) * 4,
                     (Math.random() - 0.5) * 4, 2, 4, 4, "fire.png"));
         battleScreen.addParticle(new GravityParticle(pos.x + 12 + (32 * ownerPlayer.direction), pos.y + 6,
               (Math.random() - 0.5) * 4, (Math.random() - 1) * 4, 0.5, 6, 4, "case.png", 1));
      }
      if (alive)
         hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      alive = false;
   }

   @Override
   public void kill() {
   } // Unused
}