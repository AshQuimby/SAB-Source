package game.character;

import game.Images;
import game.Player;
import game.physics.Vector;
import game.projectile.*;
import game.projectile.final_asses.JohnBall;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class John extends Character {
   public John() {
      width = 80;
      height = 76;
      hitboxWidth = 64;
      hitboxHeight = 64;
      jumps = 4;
      doubleJumpDropoff = 0.8;
      weight = 68.6;
      speed = 1.4;
      drag = 0.86;
      jumpHeight = 42;
      offset = new Vector(8, 12);
      fileName = "john.png";
      chargingParticle = "ice_shard.png";
      walkFrameTimer = 4;
      characterName = "John Joseph";
      attackLag = 0;
      overrideAttackAnimation = true;
      copiedCharacter = null;
      description = new String[] { "American attorney John Joseph, was cursed.",
            "He was deformed into a pink sphere that became so\n",
            "obsessed with working out his upper body, his feet\n",
            "became non-functional.\n",
            "\n",
            "\nDebut: John Joseph's Dream World" };
   }

   public Character copiedCharacter;
   int attackLag;

   @Override
   public void neutralAttack(Player player) {
      if (copiedCharacter == null) {
         player.battleScreen.addProjectile(
               new JohnSuck(player.center().x, player.center().y, player.direction, player.keyLayout, player));
      } else {
         copiedCharacter.neutralAttack(player);
      }
   }

   @Override
   public void sideAttack(Player player) {
      player.battleScreen.addProjectile(new Fist(player.hitbox.x, -1000, 10,
            Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
            player.keyLayout, player.direction, player));
      player.frame = 4;
      attackLag = 16;
      player.endLag = 16;
   }

   @Override
   public void upAttack(Player player) {
      player.falling = true;
      player.battleScreen.addProjectileAtCenter(
            new Slam(player.center().x, player.center().y, 22, Math.toRadians(270), player.keyLayout, player.direction,
                  player));
   }

   @Override
   public void downAttack(Player player) {
      player.battleScreen.addProjectileAtCenter(new DownPunch(player.center().x, player.center().y, Math.toRadians(90),
            player.keyLayout, player.direction, player));
      player.endLag = 6;
      player.velocity.y *= 0.5;
   }

   @Override
   public void finalAss(Player player) {
      player.battleScreen.addProjectile(new JohnBall(player));
   }

   @Override
   public void chargeAttack(Player player, int charge) {
   }

   @Override
   public void uniqueAnimations(Player player) {
      if (!player.touchingStage && player.velocity.y > 0) {
         player.frame = 8;
      }

      if (!player.touchingStage && player.velocity.y < 0) {
         player.frame = 7;
      }

      if (player.endLag > 0) {
         if (player.endLag < attackLag) {
            player.frame = 4;
         }
         if (player.endLag < attackLag * 0.67) {
            player.frame = 5;
         }
         if (player.endLag < attackLag * 0.33) {
            player.frame = 6;
         }
      }

      if (player.grabbingLedge) {
         player.frame = 10;
      }

      if (player.knockBack.len() > 2) {
         player.frame = 9;
      }
   }

   @Override
   public void postRender(Player player, Graphics g, ImageObserver target) {
      if (copiedCharacter != null) {
         player.battleScreen.renderObject(g,
               Images.getImage(
                     copiedCharacter.fileName.substring(0, copiedCharacter.fileName.length() - 4) + "_hat.png"),
               new Vector(player.pos.x + player.width / 2 - 32, player.pos.y + player.height / 2 - 52), 64,
               64,
               player.direction == -1, target);
      }
   }
}