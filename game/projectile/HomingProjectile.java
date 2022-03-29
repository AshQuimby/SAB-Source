package game.projectile;

import game.Player;

public abstract class HomingProjectile extends Projectile {

   Player targetPlayer;
   double distance;

   public void update() {
      if (!battleScreen.getGameEnded()) {
         for (int i = 0; i < 2; i++) {
            if (battleScreen.getPlayers(i) != ownerPlayer) {
               targetPlayer = battleScreen.getPlayers(i);
            }
         }
         distance = Math.sqrt((targetPlayer.pos.x - pos.x) * (targetPlayer.pos.x - pos.x)
               + (targetPlayer.pos.y - pos.y) * (targetPlayer.pos.y - pos.y));
         updateAfterTargeting();
      }
   }

   public abstract void updateAfterTargeting();
}