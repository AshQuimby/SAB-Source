package game.character;

import game.Player;
import game.SoundEngine;
import game.physics.Vector;
import game.projectile.*;
import game.projectile.final_asses.SaxSolo;

public class Walouis extends Character {
   public Walouis() {
      hasKnife = true;
      width = 64;
      height = 96;
      hitboxWidth = 56;
      hitboxHeight = 86;
      doubleJumpDropoff = 0.9;
      jumps = 1;
      weight = 78.2;
      speed = 1.4;
      drag = 0.9;
      jumpHeight = 42;
      walkFrameTimer = 3;
      offset = new Vector(8, 10);
      chargingParticle = "fire.png";
      fileName = "walouis.png";
      characterName = "Walouis";
      description = new String[] { "Walouis, the world famous jazz musician and singer",
            "is known for his smash hit albums such as:",
            "'Waaht is Love', 'A Wah's Life', and 'Waaghing in the shadows.'",
            "He also sees sucess in his professional badminton career.",
            "",
            "Debut: Marvin Badminton" };
   }

   @Override
   public void neutralAttack(Player player) {
      if (player.direction == -1) {
         player.battleScreen.addProjectile(
               new SmallNote(player.hitbox.x + 24, player.hitbox.y + 50, 16 * player.direction * (Math.random() + 0.5),
                     (Math.random() - 0.5) * 4, 10, Math.toRadians(200), player.playerId, player));
      } else {
         player.battleScreen.addProjectile(new SmallNote(player.hitbox.x + 24, player.hitbox.y + 50,
               16 * player.direction * (Math.random() + 0.5), (Math.random() - 0.5) * 4, 10,
               Math.toRadians(340), player.playerId, player));
      }
      SoundEngine.playSound("wagh");
      player.readableKeysJustPressed[5] = 0;
      player.frame = 10;
      player.endLag = 0;
   }

   @Override
   public void sideAttack(Player player) {
      player.charging = true;
      if (player.direction == -1) {
         player.battleScreen
               .addProjectile(new Racket(player.hitbox.x - 68 + (-12 * player.direction), player.hitbox.y - 28,
                     0, 0, 10, Math.toRadians(220), player.playerId, -1, player));
      } else {
         player.battleScreen
               .addProjectile(new Racket(player.hitbox.x - 68 + (-12 * player.direction), player.hitbox.y, 0, 0,
                     10, Math.toRadians(320), player.playerId, 1, player));
      }
      player.frame = 9;
   }

   @Override
   public void upAttack(Player player) {
      SoundEngine.playSound("wagh");
      player.velocity.y = -52;
      player.battleScreen.addProjectile(
            new Note(player.hitbox.x + 14, player.hitbox.y + 20, -4, 10, 10, Math.toRadians(75), player.playerId,
                  player));
      player.battleScreen.addProjectile(
            new Note(player.hitbox.x + 14, player.hitbox.y + 20, 0, 10, 10, Math.toRadians(90), player.playerId,
                  player));
      player.battleScreen.addProjectile(
            new Note(player.hitbox.x + 14, player.hitbox.y + 20, 4, 10, 10, Math.toRadians(115), player.playerId,
                  player));
      player.falling = true;
   }

   @Override
   public void downAttack(Player player) {
      int bonusVel = 0;
      if (player.readableKeys[2] || player.readableKeys[3])
         bonusVel = 4;
      if (player.direction == -1) {
         player.battleScreen
               .addProjectile(new Bomb(player.hitbox.x + 16, player.hitbox.y + 20, (10 + bonusVel) * player.direction,
                     -5 + (bonusVel - 4), 10, Math.toRadians(230), player.playerId, player));
      } else {
         player.battleScreen
               .addProjectile(new Bomb(player.hitbox.x + 16, player.hitbox.y + 20, (10 + bonusVel) * player.direction,
                     -5 + (bonusVel - 4), 10, Math.toRadians(310), player.playerId, player));
      }
      player.frame = 3;
      player.endLag = 18;
      if (player.velocity.y > -2)
         player.velocity.y = -2;
   }

   @Override
   public void finalAss(Player player) {
      player.battleScreen.addProjectile(new SaxSolo(0, 0, player.playerId, player));
      SoundEngine.playMusicOnce("walouis_sax_solo");
   }

   @Override
   public void chargeAttack(Player player, int charge) {
      player.charge = 0;
   }

   @Override
   public void uniqueAnimations(Player player) {
      if (player.falling)
         player.frame = 6;
   }
}