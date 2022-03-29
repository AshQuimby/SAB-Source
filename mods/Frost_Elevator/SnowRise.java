package mods.Frost_Elevator;

import modloader.ModProjectile;
import game.particle.Particle;
import game.particle.GravityParticle;
import game.Player;
import game.physics.AABB;
import game.physics.Vector;

// this is code for a player-locked projectile

public class SnowRise extends ModProjectile {
   public SnowRise(double x, double y, double kbDir, int direction, int owner, Player ownerPlayer) {
      // the amount of damage the projectile does
      damage = 1;
      // width, must match the image
      width = 32;
      // height, must match the image
      height = 32;
      // how long the projectile lasts before despawning
      life = 5;
      // make it not die immediately
      alive = true;
      // the direction (left or right) of this projectile
      this.direction = direction;
      // how strong the knockback of this projectile is
      knockbackStrength = 5;
      // the id of the owner
      this.owner = owner;
      // the owner as a Player object
      this.ownerPlayer = ownerPlayer;
      // the direction (in radians) that the projectile sends the hit player
      dir = kbDir;
      // the image file name of the projectile's spritesheet
      fileName = "Snowmist.png";
      // makes the owner stop falling when the poopling is created
      ownerPlayer.velocity.y = 0;
      // don't change these lines unless you know what you're doing (you probably
      // don't)
      pos = new Vector(x, y);
      hitbox = new AABB(x, y, width, height);
   }

   @Override
   public void update() {
      // makes the knockback direction change as the player presses keys
      if (ownerPlayer.readableKeys[Player.RIGHT]) {
         dir += Math.toRadians(2);
      }
      if (ownerPlayer.readableKeys[Player.LEFT]) {
         dir -= Math.toRadians(2);
      }
      
      ownerPlayer.velocity.y -= 15;
      ownerPlayer.touchingStage = false;
      if (--life == 0) {
         alive = false;
      }
      
      battleScreen
               .addParticle(new Particle(pos.x + width / 2 + 4, pos.y + height / 2 + 4, (Math.random() - 0.5) * 10,
                     5, 2, 8, 8, "Snowmist.png"));
                     
      // update hitbox before collision detection
      hitbox.setPosition(pos);
      setCenter(ownerPlayer.center());
      pos.y -= 10;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new GravityParticle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 1, 3, 3, "Snowmist.png", 1));
      }
   }

   public void setCenter(Vector pos) {
      this.pos = new Vector(pos.x - width / 2, pos.y - height / 2);
   }
}