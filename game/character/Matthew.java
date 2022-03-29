package game.character;

import game.Player;
import game.physics.*;
import game.projectile.*;

public class Matthew extends Character {
   public Matthew() {
      width = 64;
      height = 64;
      hitboxWidth = 40;
      hitboxHeight = 64;
      jumps = 1;
      doubleJumpDropoff = 0.69;
      weight = 58;
      speed = 1.9;
      drag = 0.875;
      jumpHeight = 36;
      offset = new Vector(12, 0);
      overrideAttackAnimation = true;
      fileName = "matt.png";
      chargingParticle = "smoke.png";
      walkFrameTimer = 2;
      characterName = "Matthew";
      description = new String[] { "God is dead.",
            "Matthew killed him\n",
            "\n",
            "\nDebut: Our Sports" };
   }

   private int attackLag = 0;
   private int counterTime = 0;
   private boolean usedRecovery = false;

   @Override
   public void neutralAttack(Player player) {
      player.frame = 12;
      player.endLag = 18;
      attackLag = 18;
      counterTime = 6;
   }

   @Override
   public void sideAttack(Player player) {
      player.battleScreen.addProjectile(new MattSlash(player.hitbox.x, -10000, 0, 0,
            Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
            player.keyLayout, player.direction, player));
      player.frame = 4;
      player.endLag = 4;
      attackLag = 4;
      if (player.velocity.y > -1)
         player.velocity.y = -1;
   }

   @Override
   public void upAttack(Player player) {
      if (!usedRecovery) {
         player.battleScreen.addProjectile(new UpwardsSlash(player.hitbox.x, -10000, 0, 0,
               Math.toRadians(270),
               player.keyLayout, player.direction, player));
         player.touchingStage = false;
         player.velocity.y = 0;
      }
      usedRecovery = true;
   }

   @Override
   public void downAttack(Player player) {
      player.battleScreen.addProjectile(new SwordSpike(player.hitbox.x, -10000, 0, 0,
            Math.toRadians(90),
            player.keyLayout, player.direction, player));
      if (!player.touchingStage && player.velocity.y < 16) {
         player.velocity.y = 16;
      }
      player.frame = 9;
      player.endLag = 10;
   }

   @Override
   public void chargeAttack(Player player, int charge) {
   }

   @Override
   public void uniqueUpdatePreEverything(Player player) {
      if (player.knockBack.len() > 2) {
         usedRecovery = false;
         counterTime = 0;
      }
      if (player.touchingStage || player.grabbingLedge) {
         usedRecovery = false;
      }
      if (counterTime > 0) {
         // for (Projectile projectile : player.battleScreen.getProjectiles()) {
         // AABB nextProjectilePosition = projectile.hitbox.copy();
         // nextProjectilePosition.setPosition(Vector.add(nextProjectilePosition.getPosition(),
         // projectile.velocity));
         // if (nextProjectilePosition.overlaps(player.hitbox) && projectile.ownerPlayer
         // != player) {
         // player.hitbox.setCenter(Vector.sub(projectile.ownerPlayer.hitbox.getCenter(),
         // new Vector(64 * projectile.ownerPlayer.direction, 10)));
         // projectile.ownerPlayer.stunned = 20;
         // player.direction = (int) ((projectile.ownerPlayer.hitbox.getCenter().x -
         // player.hitbox.getCenter().x) /
         // Math.abs(projectile.ownerPlayer.hitbox.getCenter().x -
         // player.hitbox.getCenter().x));
         // player.battleScreen.addProjectile(new MegaSlash(player.hitbox.x, -10000, 0,
         // 0,
         // Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
         // player.keyLayout, player.direction, player, projectile.damage));
         // player.battleScreen.addProjectile(new Teleport(player.hitbox.x, -10000, 0, 0,
         // Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
         // player.keyLayout, player.direction, player));
         // player.endLag = 24;
         // attackLag = 24;
         // projectile.owner = player.keyLayout;
         // projectile.ownerPlayer = player;
         // projectile.alive = false;
         // }
         // }
         counterTime--;
      }
   }

   @Override
   public void uniqueOnHit(Player player, game.MutableBoolean ignoreDamage, Player playerSource, int damage,
         double knockback) {
      if (counterTime > 0) {
         ignoreDamage.value = true;
         counterTime = 2;
         if (playerSource != null) {
            player.hitbox
                  .setCenter(Vector.sub(playerSource.hitbox.getCenter(), new Vector(64 * playerSource.direction, 10)));
            playerSource.stunned = 20;
            player.direction = (int) ((playerSource.hitbox.getCenter().x - player.hitbox.getCenter().x) /
                  Math.abs(playerSource.hitbox.getCenter().x - player.hitbox.getCenter().x));
            player.battleScreen.addProjectile(new MegaSlash(player.hitbox.x, -10000, 0, 0,
                  Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
                  player.keyLayout, player.direction, player, damage));
            player.battleScreen.addProjectile(new Teleport(player.hitbox.x, -10000, 0, 0,
                  Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
                  player.keyLayout, player.direction, player));
            player.endLag = 24;
            attackLag = 24;
         }
      }
   }

   @Override
   public void uniqueAnimations(Player player) {
      if (player.falling)
         player.frame = 8;

      if (player.endLag > 0) {
         if (player.endLag < attackLag) {
            player.frame = 4;
         }
         if (player.endLag < attackLag * 0.75) {
            player.frame = 5;
         }
         if (player.endLag < attackLag * 0.5) {
            player.frame = 6;
         }
         if (player.endLag < attackLag * 0.25) {
            player.frame = 7;
         }
      }

      if (counterTime > 0) {
         player.frame = 12;
      }

      if (player.grabbingLedge) {
         player.frame = 9;
      }

   }
}