package game.projectile;

import game.Player;
import game.particle.Particle;
import game.particle.GravityParticle;
import game.physics.*;
import game.character.Snas;

public class GlasterBaster extends Projectile {
   public GlasterBaster(double x, double y, int owner, int direction,
         Player ownerPlayer) {
      width = 76;
      height = 76;
      this.direction = -1;
      if (direction == -1)
         direction = 3;
      else
         direction = 1;
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

   int facingDirection;
   int fired;

   @Override
   public void update() {
      frame = facingDirection * 2;
      Snas ownerSnas = (Snas) (ownerPlayer.selectedChar);
      if (ownerPlayer.charging) {
         if (ownerPlayer.readableKeysJustPressed[Player.ATTACK] % 3 == 0) {
            ownerPlayer.battleScreen
                  .addParticle(new Particle(hitbox.getRandomPoint().x - 2, hitbox.getRandomPoint().y - 2,
                        (Math.random() - 0.5) * 2,
                        (-Math.random() - 0.3) * 4, 1, 4, 4, "snas_fire.png"));
         }
         if (ownerPlayer.readableKeysJustPressed[Player.UP] == 1) {
            facingDirection = 0;
            ownerSnas.basterFacingDireciton = new Vector(0, -1);
         } else if (ownerPlayer.readableKeysJustPressed[Player.RIGHT] == 1) {
            facingDirection = 1;
            ownerSnas.basterFacingDireciton = new Vector(1, 0);
         } else if (ownerPlayer.readableKeysJustPressed[Player.DOWN] == 1) {
            facingDirection = 2;
            ownerSnas.basterFacingDireciton = new Vector(0, 1);
         } else if (ownerPlayer.readableKeysJustPressed[Player.LEFT] == 1) {
            facingDirection = 3;
            ownerSnas.basterFacingDireciton = new Vector(-1, 0);
         }
         if (ownerPlayer.readableKeysJustPressed[Player.ATTACK] > 40) {
            frame++;
         }
         pos = new Vector(ownerPlayer.center().x - width / 2 + 90 * ownerPlayer.direction,
               ownerPlayer.center().y - height / 2);
      } else {
         fired++;
         frame++;
      }
      if (facingDirection == 0) {
         ownerSnas.basterFacingDireciton = new Vector(0, -1);
      } else if (facingDirection == 1) {
         ownerSnas.basterFacingDireciton = new Vector(1, 0);
      } else if (facingDirection == 2) {
         ownerSnas.basterFacingDireciton = new Vector(0, 1);
      } else if (facingDirection == 3) {
         ownerSnas.basterFacingDireciton = new Vector(-1, 0);
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