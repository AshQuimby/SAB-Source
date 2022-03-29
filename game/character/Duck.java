package game.character;

import game.Player;
import game.physics.Vector;
import game.SoundEngine;
import game.item.*;
import game.projectile.*;
import java.util.Random;

public class Duck extends Character {
   public Duck() {
      width = 80;
      height = 64;
      hitboxWidth = 50;
      hitboxHeight = 60;
      jumps = 2;
      doubleJumpDropoff = 0.6;
      weight = 50;
      speed = 1.9;
      drag = 0.86;
      jumpHeight = 40;
      offset = new Vector(15, 4);
      heldItemOffset = new Vector(-36, 20);
      fileName = "unnamed_duck.png";
      chargingParticle = "smoke.png";
      walkFrameTimer = 6;
      overrideWalkAnimation = true;
      overrideAttackAnimation = true;
      characterName = "Unnamed Duck";
      description = new String[] { "Nobody for sure knows Unnamed Duck's real name,",
            "\nbut a 2020 paper came to the conclusion that",
            "\nits real name is [redacted]. This duck",
            "\nloves causing chaos at every turn.",
            "\n",
            "\nDebut: No Name Duck Game" };
   }

   @Override
   public void neutralAttack(Player player) {
      player.battleScreen.addProjectile(
            new Grab(player.center().x, player.center().y, 0, 0, 0, 0, player.keyLayout, player.direction, player));
      player.frame = 5;
      player.endLag = 9;
   }

   @Override
   public void sideAttack(Player player) {
      player.battleScreen.addProjectile(new Quack(player.hitbox.x, -10000, player.direction, 0, 10,
            Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
            player.keyLayout, player.direction, player));
      SoundEngine.playSound("quack");
      player.frame = 5;
      player.endLag = 11;
   }

   @Override
   public void upAttack(Player player) {
      player.battleScreen.addProjectileAtCenter(
            new NoDuckSign(player.center().x, player.center().y - 24, 0, 10, 10, Math.toRadians(90), player.keyLayout,
                  player.direction, player));
      player.frame = 4;
      player.velocity.y = -52;
      player.touchingStage = false;
      player.falling = true;
      SoundEngine.playSound("sign");
   }
      
   @Override
   public void downAttack(Player player) {
      if (player.heldItem == null) {
         SoundEngine.playSound("quack");
         switch (new Random().nextInt(10)) {
            case 0 : player.heldItem = new Flamethrower(player.hitbox.getPosition(), player);
            break;
            
            case 1 : player.heldItem = new DuckKnife(player.hitbox.getPosition(), player);
            break;
            
            case 2 : player.heldItem = new Rake(player.hitbox.getPosition(), player);
            break;
            
            case 3 : player.heldItem = new Axe(player.hitbox.getPosition(), player);
            break;
            
            case 4 : player.heldItem = new Plane(player.hitbox.getPosition(), player);
            break;
            
            case 5 : player.heldItem = new IceCube(player.hitbox.getPosition(), player);
            break;
            
            case 6 : player.heldItem = new RubbishLid(player.hitbox.getPosition(), player);
            break;
            
            case 7 : player.heldItem = new Revolver(player.hitbox.getPosition(), player);
            break;
            
            case 8 : player.heldItem = new Glasses(player.hitbox.getPosition(), player);
            break;
            
            case 9 : player.heldItem = new DuckBomb(player.hitbox.getPosition(), player);
            break;
         }
      } else {
         player.heldItem.toss();
      }
      player.frame = 5;
      player.endLag = 10;
   }

   @Override
   public void chargeAttack(Player player, int charge) {
   }

   @Override
   public void uniqueAnimations(Player player) {
      heldItemOffset = new Vector(-36, 20);
      
      if (player.frame <= 4 && player.frame >= 1)
               heldItemOffset = new Vector(-56, 4);
      
      if (player.endLag <= 0) {
         if (player.readableKeys[Player.LEFT] || player.readableKeys[Player.RIGHT]) {
            if (++player.walkTimer >= player.selectedChar.walkFrameTimer) {
               if (++player.frame >= 5) {
                  player.frame = 1;
               }
               player.walkTimer = 0;
            }
         }
         if (!player.readableKeys[Player.LEFT] && !player.readableKeys[Player.RIGHT]
               || player.readableKeys[Player.LEFT] && player.readableKeys[Player.RIGHT]) {
            player.frame = 0;
         }
      }

      if (player.endLag > 0) {
         if (player.endLag < 5) {
            player.frame = 6;
         } else {
            player.frame = 5;
         }
      }

      if (player.falling)
         player.frame = 7;

      if (player.grabbingLedge) {
         player.frame = 9;
      }
   }
}