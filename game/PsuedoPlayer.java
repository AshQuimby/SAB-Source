package game;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import game.screen.BattleScreen;

// PseudoPlayer is a class with niche use cases. 
// Basically, you create a PseudoPlayer when you need a "fake" 
// player to run a method

public class PsuedoPlayer extends Player {
   public PsuedoPlayer(BattleScreen battleScreen, boolean onPlayerList) {
      super(-1, new game.character.Marvin(), battleScreen, 1, 0);
      if (onPlayerList) { 
         battleScreen.getPlayerList().add(this);
      }
   }
   
   public void removeFromBattlescreen() {
      lives = 0;
      justDied = true;
   }
   
   @Override
   public void kill() {
   }
   
   @Override
   public void render(Graphics g, ImageObserver target) {
   }

   @Override
   public void update() {
   }
}