package game.projectile;

import game.Player;

public abstract class HomingProjectile extends Projectile {

   protected Player targetPlayer;
   protected double distance;

   public void update() {
      double bestDistance = -1;
      if (!battleScreen.getGameEnded()) {
         for (Player player : battleScreen.getPlayerList()) {
            distance = Math.sqrt((player.pos.x - pos.x) * (player.pos.x - pos.x)
                  + (player.pos.y - pos.y) * (player.pos.y - pos.y));
            if (player != ownerPlayer && (distance < bestDistance || bestDistance == -1)) {
               targetPlayer = player;
               bestDistance = distance;
            }
         }
      }
      distance = bestDistance;
      updateAfterTargeting();
   }

   public abstract void updateAfterTargeting();
}