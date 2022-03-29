package game.projectile;

import game.Player;
import game.physics.*;

public class BoneSpin extends Projectile {
   public BoneSpin(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 14;
      width = 56;
      this.direction = direction;
      height = 56;
      life = 5;
      alive = true;
      knockbackStrength = 12;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "bone_spin.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if (--life == 0) {
         alive = false;
      }
      frame++;
      pos.x = ownerPlayer.center().x - width / 2 + (64 * ownerPlayer.direction);
      pos.y = ownerPlayer.center().y - 32;
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