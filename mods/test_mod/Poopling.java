package mods.test_mod;

import modloader.ModProjectile;
import game.particle.GravityParticle;
import game.Player;
import game.physics.AABB;
import game.physics.Vector;

// this is code for a player-locked projectile

public class Poopling extends ModProjectile {
   public Poopling(double x, double y, double kbDir, int direction, int owner, Player ownerPlayer) {
      // the amount of damage the projectile does
      damage = 3;
      // width, must match the image
      width = 40;
      // height, must match the image
      height = 32;
      // how long the projectile lasts before despawning
      life = 30;
      // make it not die immediately
      alive = true;
      // the direction (left or right) of this projectile
      this.direction = direction;
      // how strong the knockback of this projectile is
      knockbackStrength = 4;
      // the id of the owner
      this.owner = owner;
      // the owner as a Player object
      this.ownerPlayer = ownerPlayer;
      // the direction (in radians) that the projectile sends the hit player
      dir = kbDir;
      // the image file name of the projectile's spritesheet
      fileName = "poopling.png";
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
      // move the player down if they hold down
      if (ownerPlayer.readableKeys[Player.DOWN]) {
         ownerPlayer.velocity.y += 3;
      }
      ownerPlayer.velocity.y -= 5;
      ownerPlayer.touchingStage = false;
      if (++frame >= 3) {
         frame = 0;
      }
      if (--life == 0) {
         alive = false;
      }
      // update hitbox before collision detection
      hitbox.setPosition(pos);
      setCenter(ownerPlayer.center());
      pos.y -= 20;
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      alive = false;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new GravityParticle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 1, 3, 3, "poopy_particle.png", 1));
      }
   }

   public void setCenter(Vector pos) {
      this.pos = new Vector(pos.x - width / 2, pos.y - height / 2);
   }
}