package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class ThrownPlane extends HomingProjectile {

   public ThrownPlane(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 16;
      width = 76;
      height = 32;
      this.direction = direction;
      alive = true;
      life = 40;
      knockbackStrength = 12;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "plane.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
      hitPlayer = 0;
      unreflectable = false;
   }

   @Override
   public void updateAfterTargeting() {
      if (life % 4 == 0)
         if (++frame >= 4) {
            SoundEngine.playSound("swish");
            frame = 0;
         }

      if (velocity.x > 0) {
         direction = 1;
      } else {
         direction = -1;
      }

      velocity = Vector.normalize(Vector.add(Vector.sub(targetPlayer.center(), center()), velocity.mul(24)).div(25))
            .mul(8);
      boolean colliding = move(velocity, true);

      alive = !colliding;

      if (--life == 0) {
         alive = false;
      }

      hitbox.setPosition(pos);
      hittingPlayer();
      hitPlayer--;
   }

   @Override
   public void onHitPlayer(Player player) {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
               player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "blood.png"));
      }
      hitPlayer = 20;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(center().x - 4, center().y - 4, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
      }
   }
}