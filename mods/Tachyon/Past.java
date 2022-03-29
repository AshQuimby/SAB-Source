package mods.Tachyon;

import modloader.ModProjectile;
import game.physics.Vector;
import game.physics.AABB;
import game.Player;

// this is code for a player-locked projectile

public class Past extends ModProjectile {
   public Past(Vector position, int owner, Player ownerPlayer) {
      // the amount of damage the projectile do
      // width, must match the image
      width = 64;
      // height, must match the image
      height = 64;
      // how long the projectile lasts before despawning
      life = 1;
      // make it not die immediately
      alive = true;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      // the image file name of the projectile's spritesheet
      fileName = "Past_alt_" + ownerPlayer.costume + ".png";
      // don't change these lines unless you know what you're doing (you probably
      // don't)
      pos = position;
      pos.x += 20;
      pos.y += 20;
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, 64, 64);
   }

   @Override
   public void update() {
      if (--life == 0) {
         alive = false;
      }
   }

   @Override
   public void kill() {
   }
}