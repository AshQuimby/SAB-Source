package game.projectile;

import game.physics.*;
import game.Player;
import game.SoundEngine;
import game.physics.Vector;

public class Tongue extends Projectile {
   public Tongue(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 12;
      width = 96;
      this.direction = direction;
      height = 16;
      life = 6;
      alive = true;
      knockbackStrength = 10;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "tongue.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   AABB tipper = new AABB(pos.x, pos.y, 16, 16);

   @Override
   public void update() {
      tipper.x = pos.x;
      tipper.y = pos.y;
      if (direction == 1)
         tipper.x += width - 16;
      ownerPlayer.frame = 4;
      ownerPlayer.endLag = 4;
      damage = 12;
      direction = ownerPlayer.direction;
      knockbackStrength = 18;
      if (life == 4)
         frame = 1;
      if (--life == 0) {
         alive = false;
      }
      for (Player player : battleScreen.getPlayerList()) {
         if (tipper.overlaps(player.hitbox) && hitPlayer == 0) {
            damage = 24;
            knockbackStrength = 32;
            dir = Math.toRadians(200) + ((direction + 1) / 2) * Math.toRadians(140);
            SoundEngine.playSound("tongue_splat");
         }
      }
      pos.x = ownerPlayer.hitbox.x - 26 + (48 * ownerPlayer.direction);
      pos.y = ownerPlayer.hitbox.y + 8;
      pos.add(velocity);
      hitbox.setPosition(pos);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      hitPlayer = 1;
   }

   @Override
   public void kill() {
   } // Unused
}