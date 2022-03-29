package mods.test_mod;

import modloader.ModPlayerOverride;
import game.particle.Particle;
import game.Player;

public class PoopyPlayer extends ModPlayerOverride {

   @Override
   public void update(Player player) {
      if (player.selectedChar.getClass() == new PoopyMan().getClass()) {
         if (((int) (Math.random() * 20)) == 0) {
            player.battleScreen.addParticle(
                  new Particle(player.hitbox.getRandomPoint().x, player.hitbox.getRandomPoint().y,
                        (Math.random() - 0.5) * 2,
                        (-Math.random() - 0.3) * 4, 0.5, 8, 8, "fart.png"));
         }
         if (((int) (Math.random() * 20)) == 0) {
            player.battleScreen.addParticle(
                  new Particle(player.hitbox.getRandomPoint().x, player.hitbox.getRandomPoint().y,
                        (Math.random() - 0.5) * 2,
                        (-Math.random() - 0.3) * 4, 0.5, 8, 8, "fart_alt.png"));
         }
      }
   }
}