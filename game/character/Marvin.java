package game.character;

import game.Player;
import game.physics.Vector;
import game.SoundEngine;
import game.projectile.*;
import game.projectile.final_asses.Pipes;

public class Marvin extends Character {
   public Marvin() {
      width = 64;
      height = 64;
      hitboxWidth = 48;
      hitboxHeight = 64;
      jumps = 1;
      doubleJumpDropoff = 0.7;
      weight = 50;
      speed = 1.8;
      drag = 0.9;
      jumpHeight = 42;
      offset = new Vector(10, 0);
      fileName = "marvin.png";
      chargingParticle = "ice_shard.png";
      walkFrameTimer = 2;
      characterName = "Marvin";
      description = new String[] { "Retired Albany plumber now princess saving daydreamer,",
            "Marvin is a troubled man who is always distracted",
            "by his dreams about the things he could be. He can't",
            "seem escape from his brother's sucess as a musician.",
            "",
            "Debut: Super Marvin Plumber" };
   }

   @Override
   public void neutralAttack(Player player) {
      if (player.direction == -1) {
         player.battleScreen
               .addProjectile(new Fireball(player.hitbox.x + 10, player.hitbox.y + 20, 12 * player.direction, 0, 10,
                     Math.toRadians(200), player.playerId, player));
      } else {
         player.battleScreen
               .addProjectile(new Fireball(player.hitbox.x + 10, player.hitbox.y + 20, 12 * player.direction, 0, 10,
                     Math.toRadians(340), player.playerId, player));
      }
      player.frame = 4;
      player.endLag = 10;
      if (player.velocity.y > -2)
         player.velocity.y = -2;
   }

   @Override
   public void sideAttack(Player player) {
      if (player.direction == -1) {
         player.battleScreen
               .addProjectile(new Wrench(player.hitbox.x - 30, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(220),
                     player.playerId, -1, player));
      } else {
         player.battleScreen
               .addProjectile(new Wrench(player.hitbox.x + 30, player.hitbox.y + 20, 0, 0, 10, Math.toRadians(320),
                     player.playerId, 1, player));
      }
      player.frame = 4;
      player.endLag = 7;
   }

   @Override
   public void upAttack(Player player) {
      SoundEngine.playSound("toilet_flush");
      player.falling = true;
      player.battleScreen.addProjectileAtCenter(
            new Toilet(player.center().x, player.hitbox.y - 16, 0, 0, 10, Math.toRadians(270), player.playerId, 1,
                  player));
   }

   @Override
   public void downAttack(Player player) {
      player.frame = 5;
      player.charging = true;
   }

   @Override
   public void finalAss(Player player) {
      player.battleScreen
            .addProjectile(new Pipes(player.pos.x, player.pos.y, 0, player.playerId, player.direction, player));
   }

   @Override
   public void chargeAttack(Player player, int charge) {
      player.charge = 0;
      if (player.touchingStage) {
         player.velocity.y = -20;
      }
      if (charge > 60)
         charge = 60;
      if (player.direction == -1)
         player.battleScreen
               .addProjectile(
                     new Frostball(player.hitbox.x + 10, player.hitbox.y - 10, 16 * player.direction, -2, charge / 2,
                           charge, Math.toRadians(200), player.playerId, player));
      else
         player.battleScreen
               .addProjectile(
                     new Frostball(player.hitbox.x + 10, player.hitbox.y - 10, 16 * player.direction, -2, charge / 2,
                           charge, Math.toRadians(340), player.playerId, player));
   }

   @Override
   public void uniqueAnimations(Player player) {
      if (player.falling)
         player.frame = 7;
   }
}