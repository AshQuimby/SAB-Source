package mods.Tachyon;

import modloader.ModProjectile;
import game.particle.Particle;
import game.Player;
import game.physics.AABB;
import game.physics.Vector;

// this is code for a player-locked projectile

public class AfterImage extends ModProjectile {
   public AfterImage(double x, double y, double kbDir, int direction, int owner, Player ownerPlayer) {
      // the amount of damage the projectile does
      damage = 10;
      // width, must match the image
      width = 128;
      // height, must match the image
      height = 128;
      // how long the projectile lasts before despawning
      life = 3;
      // make it not die immediately
      alive = true;
      // the direction (left or right) of this projectile
      this.direction = direction;
      // how strong the knockback of this projectile is
      knockbackStrength = 10;
      // the id of the owner
      this.owner = owner;
      // the owner as a Player object
      this.ownerPlayer = ownerPlayer;
      // the direction (in radians) that the projectile sends the hit player
      dir = kbDir;
      // the image file name of the projectile's spritesheet
      fileName = "AfterImage_alt_" + ownerPlayer.costume + ".png";
      // don't change these lines unless you know what you're doing (you probably
      // don't)
      pos = new Vector(x, y);
      hitbox = new AABB(x - width / 2, y - height / 2, width, height);
   }

   @Override
   public void update() {
      frame++;
      if (--life == 0) {
         alive = false;
      }
      hittingPlayer();
   }
   
   @Override
   public void onHitPlayer(Player player) {
      hitPlayer = 1;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 20,
               (Math.random() - 0.5) * 20, 1, 3, 3, "tachyon_particle_alt_" + ownerPlayer.costume + ".png"));
      }
   }
}