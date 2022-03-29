package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.GravityParticle;

public class Bone extends Projectile {
   public Bone(double x, double y, double velX, double velY, int owner, int direction,
         Player ownerPlayer, boolean weakBone) {
      width = 28;
      this.direction = direction;
      height = 56;
      alive = true;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      this.weakBone = weakBone;
      hitPlayer = false;
      if (weakBone) {
         knockbackStrength = 0;
         damage = 4;
         dir = Math.toRadians(270);
         life = 8;
      } else {
         knockbackStrength = 6;
         damage = 12;
         dir = Math.toRadians(240) + ((direction + 1) / 2) * Math.toRadians(100);
         life = 16;
      }
      fileName = "bone.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
      for (int i = 0; i < 512; i++) {
         pos.y += 1;
         hitbox.setPosition(pos);
         if (ownerPlayer.battleScreen.getStage().colliding(hitbox)) {
            pos.y += 27;
            hitbox.setPosition(pos);
            return;
         }
      }
      pos.y -= 512 - 56;
   }

   boolean weakBone;
   boolean hitPlayer;

   @Override
   public void update() {
      if (--life == 0) {
         alive = false;
      }
      if (life % 4 == 0 && !weakBone) {
         ownerPlayer.battleScreen.addProjectileAtCenter(
               new Bone(hitbox.getCenterX(), hitbox.getCenterY(), 0, 14, owner, direction, ownerPlayer, true));
         move(velocity, false);
      }
      if (life % 4 == 0 && weakBone) {
         move(velocity, false);
      }
      hitbox.setPosition(pos);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      alive = false;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 2; i++) {
         battleScreen.addParticle(new GravityParticle(pos.x + width / 2,
               pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 1, 6, 6, "bone_particle.png", 1));
      }
   }
}