package game.character;

import game.Player;
import game.physics.Vector;
import game.SoundEngine;
import game.projectile.*;
import game.projectile.final_asses.ChainFinalSlash;

public class Chain extends Character {
   public Chain() {
      hasKnife = true;
      width = 64;
      height = 64;
      hitboxWidth = 44;
      hitboxHeight = 60;
      doubleJumpDropoff = 0.8;
      jumps = 1;
      weight = 43.6;
      speed = 1.2;
      drag = 0.92;
      jumpHeight = 34;
      walkFrameTimer = 2;
      offset = new Vector(10, 4);
      fileName = "chain.png";
      characterName = "Chain";
      description = new String[] { "Coming from the lands of Midreul, chain was trained by the",
            "royal guard from a young age to be a murder machine.", "With his trusty knives and a passion for violence",
            "Chain proves that children are still dangerous!", "", "Debut: The Legend of the Tri-Knife" };
   }

   @Override
   public void neutralAttack(Player player) {
      SoundEngine.playSound("swish");
      if (player.direction == -1) {
         player.battleScreen
               .addProjectile(new Slash(player.hitbox.x - 1000, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(220),
                     player.playerId, -1, player));
      } else {
         player.battleScreen
               .addProjectile(new Slash(player.hitbox.x - 1000, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(320),
                     player.playerId, 1, player));
      }
      player.frame = 4;
      player.endLag = 7;
   }

   @Override
   public void sideAttack(Player player) {
      if (hasKnife) {
         SoundEngine.playSound("throw");
         if (player.direction == -1) {
            player.battleScreen
                  .addProjectile(new Knife(player.hitbox.x + 10, player.hitbox.y + 20, -20, -1, 10, Math.toRadians(235),
                        player.playerId, -1, player));
         } else {
            player.battleScreen
                  .addProjectile(new Knife(player.hitbox.x + 10, player.hitbox.y + 20, 20, -1, 10, Math.toRadians(305),
                        player.playerId, 1, player));
         }
         player.frame = 4;
         player.endLag = 12;
      } else {
         SoundEngine.playSound("swish");
         if (player.direction == -1) {
            player.battleScreen
                  .addProjectile(new Slash(player.hitbox.x - 1000, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(220),
                        player.playerId, -1, player));
         } else {
            player.battleScreen
                  .addProjectile(new Slash(player.hitbox.x - 1000, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(320),
                        player.playerId, 1, player));
         }
         player.frame = 4;
         player.endLag = 7;
      }
   }

   @Override
   public void upAttack(Player player) {
      player.hitbox.y -= 5;
      player.velocity.y -= 12;
      player.falling = true;
      player.battleScreen
            .addProjectile(new AirSlash(player.hitbox.x - 30, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(270),
                  player.playerId, 1, player));
   }

   @Override
   public void downAttack(Player player) {
      player.frame = 4;
      player.endLag = 12;
      player.battleScreen
            .addProjectile(new KnifeMine(player.hitbox.x + 20 + (35 * player.direction), player.hitbox.y - 5, 0, 0, 10,
                  Math.toRadians(270), player.playerId, player.direction, player));
   }

   @Override
   public void finalAss(Player player) {
      player.battleScreen.addProjectileAtCenter(new ChainFinalSlash(player.center().x, player.center().y, 0, 0, 0,
            player.playerId, player.direction, player));
   }

   @Override
   public void uniqueAnimations(Player player) {
      if (player.falling)
         player.frame = 1;
   }
}