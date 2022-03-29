package mods.test_mod;

import modloader.ModCharacter;
import game.*;
import game.physics.Vector;

public class PoopyMan extends ModCharacter {
   // WARNING: in future versions of the game the Final Ass method WILL be abstract
   // and you WILL HAVE to have it or your mod will no longer be supported

   public PoopyMan() {
      // width of each frame (must match the image)
      width = 64;
      // height of each frame (must match the image)
      height = 64;
      // width of hitbox (can be whatever you want)
      hitboxWidth = 44;
      // height of hitbox (can be whatever you want)
      hitboxHeight = 44;
      // the offset between the top left corner of the image and the top left corner
      // of the hitbox
      offset = new Vector(10, 20);
      // the number jump height is multiplied by to get double jump height
      doubleJumpDropoff = 0.7;
      // how many jumps this character has (not including the grounded one)
      jumps = 4;
      // affects drop speed and knockback (ranges from 0-100, higher and lower will
      // break the game)
      weight = 65.9;
      // the acceleration of this character
      speed = 1.1;
      // the "air resistance" of this character, they slow down by this much every
      // frame they aren't in a freeze case, also determines the "max speed" of the
      // character
      drag = 0.89;
      // the jump height of the character
      jumpHeight = 28;
      // the number of player updates between each frame of the walk animation
      walkFrameTimer = 4;
      // the file name of the image of the spritesheet of the character, you also need
      // a "render" which is a 128x128 image to display in the character
      // select screen
      fileName = "poopy_man.png";
      // the file name of the image you want to be the particle of this character's
      // charge attack
      chargingParticle = "poopy_particle.png";
      // the name of the character in the character select screen
      characterName = "Poopy Man";
      // a description of the character which is displayed in the character select
      // screen
      description = new String[] { "Poopy man is stinky",
            "Put every line of the description on a new line",
            "of the string array",
            "this is the example modded character",
            "",
            "Debut: Toilet Cloggers: Saving Sloppy Joe" }; // this last "debut" line is optional, but all base game
                                                           // characters have it
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
      player.battleScreen.addProjectileAtCenter(new PoopBall(player.center().x, player.center().y - 24,
            12 * player.direction, -4, Math.toRadians(270), player.direction, player.keyLayout, player));
      // sets the player's frame when using the attack to allow them to animate
      player.frame = 4;
      // the number of frames after attacking the player can't move, additionally, the
      // player animates during this time
      player.endLag = 8;
   }

   // an example charge attack
   @Override
   public void sideAttack(Player player) {
      player.charging = true;
      player.frame = 4;
   }

   // example recovery
   @Override
   public void upAttack(Player player) {
      player.falling = true;
      player.battleScreen.addProjectileAtCenter(
            new Poopling(player.center().x, player.center().y, Math.toRadians(270), 1, player.keyLayout, player));
   }

   // example repeated attack
   int repeatedAttackTimer;

   @Override
   public void downAttack(Player player) {
      if (repeatedAttackTimer % 4 == 0) {
         SoundEngine.playSound("fart");
         player.battleScreen.addProjectileAtCenter(
               new Gas(player.center().x, player.center().y, Math.toRadians(270), 1, player.keyLayout, player));
      }
      player.frame = 4;
      player.velocity.x *= 0;
      player.readableKeysJustPressed[Player.ATTACK] = 0;
      repeatedAttackTimer++;
   }

   @Override
   public void chargeAttack(Player player, int charge) {
      // we add this because we don't want the charge to cap at some point
      if (charge >= 60) {
         charge = 60;
      }
      SoundEngine.playSound("fart");
      charge /= 2;
      player.battleScreen
            .addProjectileAtCenter(new PoopBomb(player.center().x, player.center().y - 24, 12 * player.direction, -4,
                  charge, Math.toRadians(270), player.direction, (int) (charge * 1.2), player.keyLayout, player,
                  charge));
      player.endLag = 16;
      player.charge = 0;
   }

   /**
    * Runs unique code in the player selecting this character before anything else,
    * this allows you to have character specific bonus code
    * 
    * @param player the player with this character selected
    */
   @Override
   public void uniqueUpdatePreEverything(Player player) {
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