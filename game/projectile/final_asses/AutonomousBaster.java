package game.projectile.final_asses;

import game.Player;
import game.SoundEngine;
import game.particle.Particle;
import game.particle.GravityParticle;
import game.physics.*;
import game.projectile.GlasterBeam;
import game.projectile.Projectile;

public class AutonomousBaster extends Projectile {
   public AutonomousBaster(double x, double y, int owner, int direction,
         Player ownerPlayer) {
      width = 76;
      height = 76;
      this.facingDirection = direction;
      alive = true;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      unreflectable = true;
      fired = 0;
      unParryable = true;
      fileName = "glaster_baster.png";
      pos = new Vector(x, y);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   Vector basterFacingDireciton = new Vector(0, 0);
   int facingDirection;
   int fired;
   int charge = 0;

   @Override
   public void update() {
      frame = facingDirection * 2;
      if (charge++ < 40) {
         if (charge % 3 == 0) {
            ownerPlayer.battleScreen
                  .addParticle(new Particle(hitbox.getRandomPoint().x - 2, hitbox.getRandomPoint().y - 2,
                        (Math.random() - 0.5) * 2,
                        (-Math.random() - 0.3) * 4, 1, 4, 4, "snas_fire.png"));
         }
      } else {
         fired++;
         frame++;
      }
      if (fired == 1) {
         SoundEngine.playSound("glaster_baster");
         battleScreen.addProjectileAtCenter(
               new GlasterBeam(center().x, center().y, ownerPlayer.keyLayout, ownerPlayer.direction,
                     basterFacingDireciton, 40, ownerPlayer));
      }
      if (facingDirection == 0) {
         basterFacingDireciton = new Vector(0, -1);
      } else if (facingDirection == 1) {
         basterFacingDireciton = new Vector(-1, 0);
      } else if (facingDirection == 2) {
         basterFacingDireciton = new Vector(0, 1);
      } else if (facingDirection == 3) {
         basterFacingDireciton = new Vector(1, 0);
      }
      if (fired > 12) {
         alive = false;
      }

      hitbox.setPosition(pos);
   }

   @Override
   public boolean drawPriority() {
      return true;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new GravityParticle(pos.x + width / 2,
               pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 1, 6, 6, "bone_particle.png", 1));
      }
   }
}