package game.character;

import game.Player;
import game.physics.Vector;
import game.SoundEngine;
import game.projectile.*;
import game.projectile.final_asses.Explosion;
import game.stage.Block;

public class Stephane extends Character {
   public Stephane() {
      width = 64;
      height = 64;
      hitboxWidth = 32;
      hitboxHeight = 56;
      jumps = 1;
      doubleJumpDropoff = 0.82;
      weight = 62;
      speed = 1.7;
      drag = 0.9;
      jumpHeight = 40.5;
      offset = new Vector(16, 8);
      fileName = "stephane.png";
      chargingParticle = "smoke.png";
      walkFrameTimer = 3;
      blocks = 8.0;
      creeperTime = 0;
      overrideAttackAnimation = true;
      characterName = "Stephane";
      description = new String[] { "BLOCK MAN,",
            "",
            "",
            "",
            "",
            "BOTTOM TEXT.",
            "",
            "Debut: Blockbreak" };
   }

   double blocks;
   private int creeperTime;

   @Override
   public void neutralAttack(Player player) {
      blocks += 1.0;
      player.battleScreen
            .addProjectileAtCenter(new Baguette(player.center().x, player.center().y, 0, 0, 12,
                  Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140), player.playerId,
                  player.direction, player));
      player.endLag = 10;
      SoundEngine.playSound("swish");
   }

   @Override
   public void sideAttack(Player player) {
      player.battleScreen
            .addProjectileAtCenter(new Arrow(player.center().x, player.center().y, 14 * player.direction, -4, 14,
                  Math.toRadians(190) + ((player.direction + 1) / 2) * Math.toRadians(160), player.playerId, player));
      player.endLag = 12;
   }

   @Override
   public void upAttack(Player player) {
      for (int i = 0; i < 4; i++) {
         if (blocks >= 1.0) {
            player.battleScreen.addProjectileAtCenter(
                  new BlockSmash(player.hitbox.getCenter().x, player.hitbox.getCenter().y, 0, 0, 24,
                        Math.toRadians(270), player.playerId, player));
            player.move(new Vector(0, -32), true);
         }
         downAttack(player);
      }
   }

   @Override
   public void downAttack(Player player) {
      player.endLag = 4;
      if (blocks >= 1.0) {
         SoundEngine.playSound("block_place");
         player.battleScreen.getStage().getPlatforms()
               .add(new Block((int) player.hitbox.x + 16, (int) player.hitbox.y + 64, 32, 32,
                     false, "block.png", player.battleScreen.getStage()));
         blocks -= 1.0;
         player.endLag = 10;
      } else {
         SoundEngine.playSound("block_fail");
      }
   }

   @Override
    public void finalAss(Player player) {
        creeperTime = 300;
        SoundEngine.playSound("spirit_charge");
    }

   @Override
   public void chargeAttack(Player player, int charge) {
   }

   @Override
   public void uniqueUpdatePreEverything(Player player) {
      if (creeperTime == 1) {
         player.battleScreen.addProjectileAtCenter(new Explosion(player.center().x, player.center().y, player));
      }

      if (--creeperTime >= 0) {
         if (player.readableKeysJustPressed[Player.ATTACK] == 1 && creeperTime < 290) {
            creeperTime = 0;
            player.readableKeys[Player.ATTACK] = false;
            player.battleScreen.addProjectileAtCenter(new Explosion(player.center().x, player.center().y, player));
         }
      }
      
      if (creeperTime > 0) {
         speed = 2.5;
         jumpHeight = 48;
         player.readableKeysJustPressed[Player.ATTACK] = 0;
         fileName = "creeper.png";
      } else {
         speed = 1.7;
         jumpHeight = 40.5;
         fileName = "stephane.png";
      }

      if (player.justDied) {
         blocks = 16.0;
      }
   }

   @Override
   public void uniqueUpdatePostFreezeCases(Player player) {
      blocks += 0.01;
      if (blocks > 16.0) {
         blocks = 16.0;
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