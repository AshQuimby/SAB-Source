package mods.test_mod;

import modloader.ModProjectile;
import game.particle.Particle;
import game.physics.Vector;
import game.physics.AABB;
import game.Player;

public class Gas extends ModProjectile {
   public Gas(double x, double y, double kbDir, int direction, int owner, Player ownerPlayer) {
      // the amount of damage the projectile does
      damage = 2;
      // width, must match the image
      width = 120;
      // height, must match the image
      height = 120;
      // how long the projectile lasts before despawning
      life = 2;
      // make it not die immediately
      alive = true;
      // the direction (left or right) of this projectile
      this.direction = direction;
      // how strong the knockback of this projectile is
      knockbackStrength = 2;
      // the id of the owner
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      // the direction (in radians) that the projectile sends the hit player
      dir = kbDir;
      // the image file name of the projectile's spritesheet
      fileName = "none.png";
      // make the projectile not render
      draw = false;
      // don't change these lines unless you know what you're doing (you probably
      // don't)
      pos = new Vector(x, y);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void onHitPlayer(Player player) {
      dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
            .rotationOf();
      alive = false;
   }

   @Override
   public void update() {
      // kill the projectile when its life reaches 0, if something else changes the
      // life variable you can use life <= 0 instead
      if (--life == 0) {
         alive = false;
      }

      // update hitbox before collision detection
      hitbox.setPosition(pos);

      // runs if hitting the opposite player
      hittingPlayer();
      // update the hitbox for debug purposes
      hitbox.setPosition(pos);
   }

   @Override
   public void kill() {
      for (int i = 0; i < 2; i++) {
         battleScreen
               .addParticle(new Particle(pos.x + width / 2 + 4, pos.y + height / 2 + 4, (Math.random() - 0.5) * 10,
                     (Math.random() - 0.5) * 10, 2, 8, 8, "fart.png"));
      }
      for (int i = 0; i < 2; i++) {
         battleScreen
               .addParticle(new Particle(pos.x + width / 2 + 4, pos.y + height / 2 + 4, (Math.random() - 0.5) * 10,
                     (Math.random() - 0.5) * 10, 2, 8, 8, "fart_alt.png"));
      }
   }
}