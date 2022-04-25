package game.character;

import game.Player;
import game.physics.Vector;
import game.SoundEngine;
import game.projectile.*;
import game.projectile.final_asses.LightsOut;

public class Gus extends Character {
   public Gus() {
      hasKnife = true;
      width = 64;
      height = 64;
      hitboxWidth = 44;
      hitboxHeight = 52;
      doubleJumpDropoff = 0.6;
      jumps = 1;
      weight = 36.4;
      speed = 1.4;
      drag = 0.9;
      jumpHeight = 24;
      walkFrameTimer = 3;
      offset = new Vector(12, 12);
      fileName = "gus.png";
      characterName = "Gus";
      description = new String[] { "First name Amon, This hazmat suit wearing astronaut is always",
            "getting into trouble no matter where they go. Sometimes,",
            "they're the alien, sometimes they're chased by the alien",
            "but they can never seem to catch a break.", "",
            "Debut: Around Ourselves" };
   }

   @Override
   public void neutralAttack(Player player) {
      if (player.direction == -1) {
         player.battleScreen
               .addProjectile(new Bullet(player.hitbox.x + 16, player.hitbox.y + 28, player.direction * 12, 0, 2,
                     Math.toRadians(190), player.playerId, player));
      } else {
         player.battleScreen
               .addProjectile(new Bullet(player.hitbox.x + 16, player.hitbox.y + 28, player.direction * 12, 0, 2,
                     Math.toRadians(350), player.playerId, player));
      }
      player.frame = 3;
      player.endLag = 18;
   }

   @Override
   public void sideAttack(Player player) {
      SoundEngine.playSound("tongue_spit");
      if (player.direction == -1) {
         player.battleScreen
               .addProjectile(new Tongue(player.hitbox.x - 16 + (48 * player.direction), player.hitbox.y + 20, 0,
                     0, 10, Math.toRadians(310), player.playerId, -1, player));
      } else {
         player.battleScreen
               .addProjectile(new Tongue(player.hitbox.x - 16 + (48 * player.direction), player.hitbox.y + 20, 0,
                     0, 10, Math.toRadians(230), player.playerId, 1, player));
      }
      player.frame = 4;
      player.endLag = 7;
   }

   @Override
   public void upAttack(Player player) {
      player.falling = true;
      player.battleScreen
            .addProjectileAtCenter(new SussyVent(player.center().x, player.center().y - 46, player.playerId, player));
   }

   @Override
   public void downAttack(Player player) {
      player.frame = 4;
      player.endLag = 12;
      player.battleScreen
            .addProjectile(new MiniGus(player.hitbox.x + 20 + (35 * player.direction), player.hitbox.y - 5, 0, 0, 10,
                  Math.toRadians(270), player.playerId, player.direction, player));
   }

   @Override
   public void chargeAttack(Player player, int charge) {
   }

   @Override
   public void finalAss(Player player) {
      player.battleScreen.addProjectile(new LightsOut(player));
   }

   @Override
   public void uniqueAnimations(Player player) {
      if (player.falling)
         player.frame = 6;
   }
}