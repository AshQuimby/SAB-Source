package game.character;

import game.Player;
import game.physics.Vector;
import game.SoundEngine;
import game.projectile.*;
import game.projectile.final_asses.FallingBananaSpawner;

public class EmperorEvil extends Character {
   public EmperorEvil() {
      hasKnife = true;
      width = 80;
      height = 88;
      hitboxWidth = 80;
      hitboxHeight = 80;
      doubleJumpDropoff = 0.82;
      jumps = 1;
      weight = 86.4;
      speed = 1.49;
      drag = 0.84;
      jumpHeight = 46;
      walkFrameTimer = 4;
      offset = new Vector(0, 8);
      fileName = "emperor_evil.png";
      characterName = "Emperor E. Vile";
      description = new String[] { "Emperor E. Vile is a big mean alligator",
            "who steals all of the bananas from the monkeys.",
            "He doesn't even eat bananas, nobody knows why",
            "he even takes them in the first place.",
            "",
            "Debut: King Kong City" };
   }

   @Override
   public void neutralAttack(Player player) {
      if (player.direction == -1) {
         player.battleScreen.addProjectile(
               new Cannonball(player.hitbox.x + 24, player.hitbox.y + 10, 20 * player.direction, 0, 10,
                     Math.toRadians(200),
                     player.playerId, player));
      } else {
         player.battleScreen.addProjectile(new Cannonball(player.hitbox.x + 24, player.hitbox.y + 10,
               20 * player.direction, 0, 10, Math.toRadians(340), player.playerId, player));
      }
      player.frame = 9;
      player.endLag = 0;
   }

   @Override
   public void sideAttack(Player player) {
      if (player.direction == -1) {
         player.battleScreen
               .addProjectile(new Bite(player.hitbox.x - 2000, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(220),
                     player.playerId, -1, player));
      } else {
         player.battleScreen
               .addProjectile(new Bite(player.hitbox.x - 2000, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(320),
                     player.playerId, 1, player));
      }
      player.frame = 4;
      player.endLag = 8;
   }

   @Override
   public void upAttack(Player player) {
      player.battleScreen.addProjectile(
            new Barrel(player.playerId, player));
      player.falling = true;
   }

   @Override
   public void downAttack(Player player) {
      player.frame = 4;
      player.charging = true;
   }

   @Override
   public void finalAss(Player player) {
      player.battleScreen.addProjectile(new FallingBananaSpawner(player));
   }

   @Override
   public void chargeAttack(Player player, int charge) {
      if (charge > 15) {
         charge = 15;
      }
      charge *= 2;
      player.battleScreen
            .addProjectileAtCenter(
                  new Banana(player.center().x, player.center().y, charge, charge, player.playerId,
                        player));
      player.charge = 0;
      SoundEngine.playSound("banana_throw");
      SoundEngine.playSound("throw");
      SoundEngine.playSound("swish");
   }

   @Override
   public void uniqueAnimations(Player player) {
      if (player.falling)
         player.frame = 6;
   }
}