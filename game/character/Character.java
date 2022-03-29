package game.character;

import java.io.Serializable;

import java.awt.Graphics;
import game.Player;
import game.physics.*;
import java.awt.image.ImageObserver;

public abstract class Character implements Serializable, Cloneable {
   public String fileName;
   public String characterName;
   public int width;
   public int height;
   public int id;
   public int jumps;
   public int hitboxWidth;
   public int hitboxHeight;
   public int walkFrameTimer;
   public double doubleJumpDropoff;
   public double weight;
   public double speed;
   public double drag;
   public double jumpHeight;
   public Vector offset;
   public String[] description;
   public String chargingParticle = "smoke.png";
   public boolean overrideWalkAnimation = false;
   public boolean overrideAttackAnimation = false;
   public int altCount = 1;
   public Vector heldItemOffset = new Vector(0, 0);

   public boolean hasKnife; // just for chain

   /**
    * Runs the player's neutral attack code
    * 
    * @param player the player with this character selected
    */
   public abstract void neutralAttack(Player player);

   /**
    * Runs the player's side attack code
    * 
    * @param player the player with this character selected
    */
   public abstract void sideAttack(Player player);

   /**
    * Runs the player's up attack code
    * 
    * @param player the player with this character selected
    */
   public abstract void upAttack(Player player);

   /**
    * Runs the player's down attack code
    * 
    * @param player the player with this character selected
    */
   public abstract void downAttack(Player player);

   /**
    * Runs the player's final ass code
    * WARNING: This method will need to be overriden if you are making a
    * funtional character
    * 
    * @param player the player with this character selected
    */
   public void finalAss(Player player) {
      neutralAttack(player);
   }

   /**
    * Runs character specific code when the player is in
    * their "charging" state
    * 
    * @param player the player with this character selected
    * @param charge how long the player has been charging
    */
   public void chargeAttack(Player player, int charge) {
   }

   /**
    * Runs unique code in the player selecting this character before anything else,
    * this allows you to have character specific bonus code
    * 
    * @param player the player with this character selected
    */
   public void uniqueUpdatePreEverything(Player player) {
   }

   /**
    * Runs unique code in the player selecting this character before animations
    * but after things that would disallow the player to move have happened,
    * this allows you to have character specific bonus code
    * 
    * @param player the player with this character selected
    */
   public void uniqueUpdatePostFreezeCases(Player player) {
   }

   /**
    * Runs unique code in the player selecting this character after player
    * animations this allows you to have unique animations per character that are
    * not controlled by a external source
    * 
    * @param player the player with this character selected
    */
   public void uniqueAnimations(Player Player) {
   }

   /**
    * Runs unique code in the player selecting this character after anything else
    * this allows you to have character specific bonus code
    *
    * @param player the player with this character selected
    */
   public void uniqueUpdatePostEverything(Player player) {
   }

   /**
    * Runs unique code in the player selecting this character after anything else
    * this allows you to have character specific bonus code
    *
    * @param player the player with this character selected
    */
   public void uniqueUpdateOnGameStart(Player player) {
   }

   public void uniqueOnHit(Player player, game.MutableBoolean ignoreDamage, Player playerSource) {
   }

   public void uniqueOnHit(Player player, game.MutableBoolean ignoreDamage, Player playerSource, int damage,
         double knockback) {
   }

   public AABB getHitbox(Player player) {
      AABB hitbox = new AABB(player.hitbox.x, player.hitbox.y, hitboxWidth, hitboxHeight);
      hitbox.setPosition(hitbox.getPosition());
      return hitbox;
   }

   public void postRender(Player player, Graphics g, ImageObserver target) {
   }

   public void preRender(Player player, Graphics g, ImageObserver target) {
   }

   public void renderUIElements(Player player, Graphics g, ImageObserver target) {
   }

   public Character copy() {
      try {
         return (Character) this.clone();
      } catch (CloneNotSupportedException e) {
      }
      return null;
   }
}