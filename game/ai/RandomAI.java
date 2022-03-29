package game.ai;

import game.Player;

public class RandomAI extends AI {
   @Override
   public void update(Player player) {
      for (int i = 0; i < player.readableKeys.length; i++) {
         if (Math.random() > 0.9 && !player.readableKeys[i]
               && (i != Player.UP && i != Player.DOWN || Math.random() > 0.99)) {
            player.simulateKeyPress(i);
         }
         if (Math.random() > 0.9 && player.readableKeys[i]) {
            player.simulateKeyRelease(i);
         }
      }
      if (player.charge > 15 && Math.random() > 0.3) {
         player.simulateKeyRelease(5);
      }
      if (player.readableKeysJustPressed[Player.ATTACK] > 10 && Math.random() > 0.9) {
         player.simulateKeyRelease(5);
      }
   }
}