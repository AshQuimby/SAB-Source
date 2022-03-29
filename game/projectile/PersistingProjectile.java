package game.projectile;

import game.Player;

public abstract class PersistingProjectile extends Projectile {

   public void update() {
      if (ownerPlayer.readableKeys[Player.ATTACK]) {
         updateWhilePersisting();
      } else {
         alive = false;
      }
   }

   public abstract void updateWhilePersisting();

   public abstract void kill();
}