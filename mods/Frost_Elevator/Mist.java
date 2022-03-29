package mods.Frost_Elevator;

import modloader.ModProjectile;
import game.particle.Particle;
import game.physics.Vector;
import game.physics.AABB;
import game.Player;

public class Mist extends ModProjectile {
   int back = 0;
   public static int frostWhen = 0;
   public Mist(double x, double y, double kbDir, int direction, int owner, Player ownerPlayer) {
      // the amount of damage the projectile does
      damage = 3;
      // width, must match the image
      width = 8;
      // height, must match the image
      height = 4;
      // how long the projectile lasts before despawning
      life = 3;
      // make it not die immediately
      alive = true;
      // the direction (left or right) of this projectile
      this.direction = direction;
      // how strong the knockback of this projectile is
      knockbackStrength = 1;
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
      
      if (direction == -1) {
         back = width * 15;
      }
      pos = new Vector(x, y);
      hitbox = new AABB(pos.x - back, pos.y, width * 15, height * 10);
   }

   @Override
   public void update() {
      // kill the projectile when its life reaches 0, if something else changes the
      // life variable you can use life <= 0 instead
      if (--life == 0) {
         alive = false;
      }

      // update hitbox before collision detection
      hitbox.setPosition(pos.x - back, pos.y);

      hittingPlayer();
      
      // update the hitbox for debug purposes
      hitbox.setPosition(pos.x - back, pos.y);
   }
   
   @Override
   public void onHitPlayer(Player player) {
      // makes the knockback the direction from the projectile to the player
         dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
               .rotationOf();   
         if (player.frozen < 1) {
            frostWhen++;
         } else {
            frostWhen = 0;
         }
         if (player.damage > 100 && player.frozen < 1 & frostWhen > 2) {
            player.frozen = player.damage / 4 - 20;
         } 
         alive = false;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 2; i++) {
         battleScreen
               .addParticle(new Particle(pos.x + width / 2 + 4, pos.y + height / 2 + 4, 10 * direction,
                     (Math.random() - 0.5) * 10, 2, 8, 8, "Snowmist.png"));
      }
      for (int i = 0; i < 2; i++) {
         battleScreen
               .addParticle(new Particle(pos.x + width / 2 + 4, pos.y + height / 2 + 4, 10 * direction,
                     (Math.random() - 0.5) * 10, 2, 8, 8, "Snowmist_alt.png"));
      }
   }
}