package mods.Tachyon;

import modloader.ModCharacter;
import game.*;
import game.physics.Vector;
import java.util.ArrayList;

public class Tachyon extends ModCharacter {
   public Tachyon() {
      // width of each frame (must match the image)
      width = 64;
      // height of each frame (must match the image)
      height = 64;
      // width of hitbox (can be whatever you want)
      hitboxWidth = 40;
      // height of hitbox (can be whatever you want)
      hitboxHeight = 55;
      // the offset between the top left corner of the image and the top left corner
      // of the hitbox
      offset = new Vector(12, 9);
      // the number jump height is multiplied by to get double jump height
      doubleJumpDropoff = 0.7;
      // how many jumps this character has (not including the grounded one)
      jumps = 2;
      // affects drop speed and knockback (ranges from 0-100, higher and lower will
      // break the game)
      weight = 50;
      // the acceleration of this character
      speed = 1.3;
      // the "air resistance" of this character, they slow down by this much every
      // frame they aren't in a freeze case, also determines the "max speed" of the
      // character
      drag = 0.9;
      // the jump height of the character
      jumpHeight = 38;
      // the number of player updates between each frame of the walk animation
      walkFrameTimer = 4;
      
      altCount = 2;
      // the file name of the image of the spritesheet of the character, you also need
      // a "render" which is a non-upscaled 128x128 image to display in the character
      // select screen
      fileName = "tachyon.png";
      // the file name of the image you want to be the particle of this character's
      // charge attack
      // the name of the character in the character select screen
      characterName = "Tachyon";
      // a description of the character which is displayed in the character select
      // screen
      description = new String[] { "Tachyon trancends time",
            "Tachyon harnesses the power of tachyon particles",
            "This allows it to travel through time",
            "Only 1 second in the past though, or to another version of it",
            "",
            "Debut: Time Wipers" }; // this last "debut" line is optional, but all base game characters have it
   }

   // This is example code for a projectile that travels in the direction the
   // player is facing when fired
   int repeatedAttackTimer;
   int frameCaller = 0;
   
   @Override
   public void neutralAttack(Player player) {
      if (repeatedAttackTimer % 2 == 0) {
         SoundEngine.playSound("reflect");
         if (frameCaller >= 4) {
            frameCaller = 1;
         } else {
            frameCaller++;
         }
         player.battleScreen.addProjectileAtCenter(
               new Reflector(player.center().x, player.center().y, Math.toRadians(270), 1, player.keyLayout, frameCaller, player));
      }
      player.frame = 4;
      player.velocity.x *= 0;
      player.readableKeysJustPressed[Player.ATTACK] = 0;
      repeatedAttackTimer++;
   }

   // an example charge attack
   @Override
   public void sideAttack(Player player) {
      SoundEngine.playSound("fwoosh");
      player.frame = 4;
      player.battleScreen
         .addProjectileAtCenter(new Stab(player.center().x, player.center().y - 24, 12 * player.direction, -4,
            10, Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140), player.keyLayout, player.direction, player));
      player.endLag = 9;
   }

   // example recovery
   @Override
   public void upAttack(Player player) {
      if (framesT.size() > 39) {
         SoundEngine.playSound("teleport");
         player.falling = false;
         player.battleScreen.addProjectileAtCenter(
               new Teleport(player.center().x, player.center().y, Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140), 1, player.keyLayout, player));
         player.hitbox.setPosition(framesT.get(39));
         framesT.clear();
         player.velocity.y = -5;
         player.battleScreen.addProjectileAtCenter(
               new AfterImage(player.center().x, player.center().y, Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140), 1, player.keyLayout, player));
         player.endLag = 5;
      }
   }

   @Override
   public void downAttack(Player player) {
      SoundEngine.playSound("fwoosh");
      player.frame = 4;
      player.battleScreen
         .addProjectileAtCenter(new Push(player.center().x, player.center().y - 24, 12 * player.direction, -4,
               10, Math.toRadians(90), player.keyLayout, player.direction, player));
      player.endLag = 7;
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
   public ArrayList<Vector> framesT = new ArrayList<Vector>();
   ////////////////////////////////////////////////////////////////create based of player id???
   @Override
   public void uniqueUpdatePreEverything(Player player) {
      framesT.add(0, player.hitbox.getPosition());
      if (framesT.size() > 40) {
         framesT.remove(40);
         //System.out.println(framesT.get(39).x + " " + framesT.get(39).y);
            player.battleScreen.addProjectileAtCenter(new Past(framesT.get(39).clone(), player.keyLayout, player));
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
   
   @Override
   public void uniqueUpdateOnGameStart(Player player) {
      chargingParticle = "tachyon_particle_alt_" + player.costume + ".png";
   }
}