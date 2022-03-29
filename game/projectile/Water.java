package game.projectile;

import game.Player;
import game.physics.*;

public class Water extends Projectile {
   public Water(double x, double y, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 8;
      width = 40;
      height = 96;
      life = 2;
      alive = true;
      knockbackStrength = 12;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      dir += (Math.random() - 0.5) / 4;
      fileName = "none.png";
      pos = new Vector(x, y);
      unreflectable = true;
      draw = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if (--life == 0) {
         alive = false;
      }
      hittingPlayer();
   }

   @Override
   public void kill() {
   } // Unused
}