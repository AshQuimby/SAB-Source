package mods.Tachyon;

import modloader.ModProjectile;
import game.particle.Particle;
import game.projectile.Projectile;
import game.physics.Vector;
import game.Player;
import game.physics.AABB;

public class Reflector extends ModProjectile {
   public Reflector(double x, double y, double kbDir, int direction, int owner, int frameCalled, Player ownerPlayer) {
      // the amount of damage the projectile does
      damage = 0;
      // width, must match the image
      width = 128;
      // height, must match the image
      height = 128;
      // how long the projectile lasts before despawning
      life = 2;
      // make it not die immediately
      alive = true;
      // the direction (left or right) of this projectile
      this.direction = direction;
      unParryable = true;
      unreflectable = true;
      // how strong the knockback of this projectile is
      knockbackStrength = 0;
      // the id of the owner
      this.owner = owner;
      // the direction (in radians) that the projectile sends the hit player
      dir = kbDir;
      // the image file name of the projectile's spritesheet
      fileName = "Reflector_alt_" + ownerPlayer.costume + ".png";
      frame = frameCalled;
      // make the projectile not render
      draw = true;
      // don't change these lines unless you know what you're doing (you probably
      // don't)
      pos = new Vector(x, y);
      this.ownerPlayer = ownerPlayer;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      // kill the projectile when its life reaches 0, if something else changes the
      // life variable you can use life <= 0 instead
      if (--life == 0) {
         alive = false;
      }
      // update the hitbox for debug purposes
      hitbox.setPosition(pos);
      for (Projectile projectile : ownerPlayer.battleScreen.getProjectiles()) {
         if (projectile.hitbox.overlaps(hitbox) && !projectile.unreflectable && projectile.ownerPlayer != ownerPlayer) {
            projectile.owner = owner;
            projectile.ownerPlayer = ownerPlayer;
            projectile.velocity.x *= -1;
            projectile.dir = projectile.velocity.rotationOf();
            for (int i = 0; i < 4; i++) {
                  battleScreen.addParticle(new Particle(projectile.pos.x + projectile.width / 2,
                        projectile.pos.y + projectile.height / 2, (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, "tachyon_particle_alt_" + ownerPlayer.costume + ".png"));
               }
         }
      }
   }

   @Override
   public void kill() {
   }
}