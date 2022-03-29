package game.character;

import game.Player;
import game.physics.Vector;
import game.SoundEngine;
import game.particle.GravityParticle;
import game.projectile.*;

public class Snas extends Character {
   public Snas() {
      width = 68;
      height = 88;
      hitboxWidth = 52;
      hitboxHeight = 68;
      jumps = 1;
      doubleJumpDropoff = 0.82;
      weight = 62;
      speed = 1.7;
      drag = 0.9;
      jumpHeight = 40.5;
      offset = new Vector(8, 20);
      fileName = "snas.png";
      chargingParticle = "smoke.png";
      walkFrameTimer = 2;
      overrideAttackAnimation = true;
      characterName = "Snas";
      description = new String[] { "This laid back skeleton wizard,",
            "\ndoesn't always look the magical part,",
            "\nbut be wary, his boneomancy can pack quite a punch.",
            "\nAnd he has a bone to pick with you.",
            "\n",
            "\nDebut: Belowstory" };
   }

   public Vector basterFacingDireciton;

   @Override
   public void neutralAttack(Player player) {
      player.battleScreen
            .addProjectileAtCenter(new Bone(player.center().x, player.center().y - 56, 56 * player.direction, 0,
                  player.keyLayout, player.direction, player, false));
      player.endLag = 10;
      SoundEngine.playSound("snas");
   }

   @Override
   public void sideAttack(Player player) {
      player.battleScreen
            .addProjectileAtCenter(new BoneSpin(player.center().x, player.center().y, 0, 0, 14,
                  Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140), player.keyLayout,
                  player.direction, player));
      player.endLag = 8;
      SoundEngine.playSound("snas");
   }

   @Override
   public void upAttack(Player player) {
      player.falling = true;
      player.velocity.y = -44;
      int faceLeft = (player.direction - 1) / 2;
      if (player.costume == 0) {
         player.battleScreen.addParticle(
               new GravityParticle(player.hitbox.x + 64 + 70 * faceLeft, player.hitbox.y - 16,
                     (Math.random() - 0.5) * 5, (Math.random() - 1) * 8, 1, 15 * player.direction, 11, "snas_body.png",
                     1));
      } else {
         player.battleScreen.addParticle(
               new GravityParticle(player.hitbox.x + 64 + 70 * faceLeft, player.hitbox.y - 16,
                     (Math.random() - 0.5) * 5, (Math.random() - 1) * 8, 1, 15 * player.direction, 11,
                     "snas_body_alt_1.png", 1));
      }
      SoundEngine.playSound("crunch");
      SoundEngine.playSound("snas");
   }

   @Override
   public void downAttack(Player player) {
      SoundEngine.playSound("snas");
      player.battleScreen.addProjectileAtCenter(
            new GlasterBaster(player.center().x + 128 * player.direction, player.center().y, player.keyLayout,
                  player.direction, player));
      player.charging = true;
      player.charge = 1;
   }

   @Override
   public void chargeAttack(Player player, int charge) {
      player.charge = 0;
      if (charge > 40)
         charge = 40;
      SoundEngine.playSound("glaster_baster");
      player.battleScreen.addProjectileAtCenter(
            new GlasterBeam(player.center().x + 90 * player.direction - 76 / 4, player.center().y - 76 / 4,
                  player.keyLayout,
                  player.direction,
                  basterFacingDireciton, charge, player));
      player.endLag = 14;
   }

   @Override
   public void uniqueUpdatePreEverything(Player player) {
      if (player.falling && player.touchingStage) {
         player.hitbox.y -= 36;
      }
   }

   @Override
   public void uniqueUpdatePostFreezeCases(Player player) {
      if (player.falling) {
         player.hitbox.width = 48;
         player.hitbox.height = 32;
         offset = new Vector(0, 0);
      } else {
         player.hitbox.width = 52;
         player.hitbox.height = 68;
         offset = new Vector(8, 20);
      }
   }

   @Override
   public void uniqueAnimations(Player player) {
      if (player.falling)
         player.frame = 6;
      if (player.charging)
         player.frame = 4;
      if (player.endLag > 8)
         player.frame = 4;
      else if (player.endLag > 0)
         player.frame = 5;
   }
}