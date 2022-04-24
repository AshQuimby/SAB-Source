package game.projectile.final_asses;

import game.Images;
import game.Player;
import game.particle.GravityParticle;
import game.physics.*;
import game.projectile.HomingProjectile;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class BasterSpam extends HomingProjectile {
   public BasterSpam(Player ownerPlayer) {
      width = 0;
      height = 0;
      this.direction = 1;
      alive = true;
      this.owner = ownerPlayer.keyLayout;
      this.ownerPlayer = ownerPlayer;
      unreflectable = true;
      unParryable = true;
      fileName = "none.png";
      draw = false;
      life = 180;
      pos = new Vector(0, 0);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void updateAfterTargeting() {
      if (--life == 0) {
         alive = false;
      }
      if (life % 15 == 0 || (life < 30 && life % 5 == 0)) {
         int facing = 0;
         facing = (int) (Math.random() * 4);
         Vector spawnPos = new Vector(0, 0);
         if (facing == 0) {
            spawnPos = new Vector(targetPlayer.center().x, targetPlayer.center().y + 180);
         } else if (facing == 1) {
            spawnPos = new Vector(targetPlayer.center().x + 180, targetPlayer.center().y);
         } else if (facing == 2) {
            spawnPos = new Vector(targetPlayer.center().x, targetPlayer.center().y - 180);
         } else if (facing == 3) {
            spawnPos = new Vector(targetPlayer.center().x - 180, targetPlayer.center().y);
         }
         battleScreen.addProjectileAtCenter(
               new AutonomousBaster(spawnPos.x, spawnPos.y, owner, facing, ownerPlayer));
      }
   }

   @Override
   public boolean drawPriority() {
      return true;
   }

   @Override
   public void postRender(Graphics g, ImageObserver target) {
      if (battleScreen != null && targetPlayer != null)
         battleScreen.renderObject(g, Images.getImage("soul.png"), Vector.sub(targetPlayer.center(), new Vector(16, 16)),
            32, 32, false, target);
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