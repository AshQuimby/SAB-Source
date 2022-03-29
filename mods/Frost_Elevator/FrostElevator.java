package mods.Frost_Elevator;

import modloader.ModCharacter;
import game.*;
import game.physics.Vector;
import game.projectile.*;

public class FrostElevator extends ModCharacter {
   public FrostElevator() {
      // width of each frame (must match the image)
      width = 128;
      // height of each frame (must match the image)
      height = 128;
      // width of hitbox (can be whatever you want)
      hitboxWidth = 100;
      // height of hitbox (can be whatever you want)
      hitboxHeight = 100;
      // the offset between the top left corner of the image and the top left corner
      // of the hitbox
      offset = new Vector(14, 28);
      // the number jump height is multiplied by to get double jump height
      doubleJumpDropoff = 0.9;
      // how many jumps this character has (not including the grounded one)
      jumps = 1;
      // affects drop speed and knockback (ranges from 0-100, higher and lower will
      // break the game)
      weight = 70;
      // the acceleration of this character
      speed = 2;
      // the "air resistance" of this character, they slow down by this much every
      // frame they aren't in a freeze case, also determines the "max speed" of the
      // character
      drag = 0.9;
      // the jump height of the character
      jumpHeight = 40;
      // the number of player updates between each frame of the walk animation
      walkFrameTimer = 4;
      // the file name of the image of the spritesheet of the character, you also need
      // a "render" which is a non-upscaled 128x128 image to display in the character
      // select screen
      fileName = "frost_elevator.png";
      // the file name of the image you want to be the particle of this character's
      // charge attack
      chargingParticle = "Snowmist.png";
      // the name of the character in the character select screen
      characterName = "Frost Elevator";
      // a description of the character which is displayed in the character select
      // screen
      description = new String[] { "Frost Elevator is a wendigo",
            "They were trapped on a mountain with nothing but their twin",
            "Without food, they were forced to eat their twin alive",
            "This act of cannibalism resulted in the turning a mountinous wendigo",
            "",
            "Debut: Frost Elevator(s)" }; // this last "debut" line is optional, but all base game characters have it
   }


   // This is example code for a projectile that travels in the direction the
   // player is facing when fired
   @Override
   public void neutralAttack(Player player) {
      // player.battleScreen gets the game data when in battle, there are two methods,
      // addProjectile & addProjectileAtCenter
      // addProjectile adds the projectile so that its top left corner is at the
      // coordinates,
      // addProjectileAtCenter adds the projectile so that its center is at the
      // coordinates
      SoundEngine.playSound("bleh");
      player.battleScreen.addProjectileAtCenter(new SnowBall(player.center().x, player.center().y - 24,
            12 * player.direction, -4, Math.toRadians(270), player.direction, player.keyLayout, player));
      // sets the player's frame when using the attack to allow them to animate
      player.frame = 4;
      // the number of frames after attacking the player can't move, additionally, the
      // player animates during this time
      player.endLag = 15;
   }

   // an example charge attack
   @Override
   public void sideAttack(Player player) {
      player.frame = 4;
      SoundEngine.playSound("fwoosh");
      player.battleScreen
         .addProjectileAtCenter(new Slash(player.center().x, player.center().y - 24, 12 * player.direction, -4,
               10, Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140), player.keyLayout, player.direction, player));
      if (player.touchingStage) {
         player.endLag = 15;
      } else {
         player.endLag = 8;
      }
   }

   // example recovery
   @Override
   public void upAttack(Player player) {
      player.falling = true;
      SoundEngine.playSound("burst");
      player.battleScreen.addProjectileAtCenter(
            new SnowRise(player.center().x, player.center().y, Math.toRadians(270), 1, player.keyLayout, player));
   }

   // example repeated attack
   int repeatedAttackTimer;

   @Override
   public void downAttack(Player player) {
      if (repeatedAttackTimer % 4 == 0) {
         SoundEngine.playSound("mist");
         player.battleScreen.addProjectileAtCenter(
               new Mist(player.center().x + 50 * player.direction, player.center().y, Math.toRadians(270), player.direction, player.keyLayout, player));
      }
      player.frame = 4;
      player.velocity.x *= 0;
      player.readableKeysJustPressed[Player.ATTACK] = 0;
      repeatedAttackTimer++;
   }

   @Override
   public void chargeAttack(Player player, int charge) {
   }

   /**
    * Runs unique code in the player selecting this character before anything else,
    * this allows you to have character specific bonus code
    * 
    * @param player the player with this character selected
    */
   @Override
   public void uniqueUpdatePreEverything(Player player) {
      if (!(player.readableKeys[Player.DOWN] && player.readableKeys[Player.ATTACK])) {
         Mist.frostWhen = 0;
      }
   }

   /**
    * Runs unique code in the player selecting this character before animations
    * but after things that would disallow the player to move have happened,
    * this allows you to have character specific bonus code
    * 
    * @param player the player with this character selected
    */
   @Override
   public void uniqueUpdatePostFreezeCases(Player player) {
      // reset repeated attack timer
      if (!player.readableKeys[Player.ATTACK]) {
         repeatedAttackTimer = 0;
      }
   }

   /**
    * Runs unique code in the player selecting this character after player
    * animations this allows you to have unique animations per character that are
    * not controlled by a external source
    * 
    * @param player the player with this character selected
    */
   @Override
   public void uniqueAnimations(Player player) {
      if (player.falling) {
         if (player.velocity.y > 0) {
            player.frame = 6;
         } 
      }
   }

   /**
    * Runs unique code in the player selecting this character after anything else
    * this allows you to have character specific bonus code
    *
    * @param player the player with this character selected
    */
   @Override
   public void uniqueUpdatePostEverything(Player Player) {
   }
}