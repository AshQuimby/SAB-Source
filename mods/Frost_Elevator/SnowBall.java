package mods.Frost_Elevator;

import game.physics.AABB;
import game.physics.Vector;
import modloader.ModProjectile;
import game.particle.GravityParticle;
import game.Player;

public class SnowBall extends ModProjectile {
   /*
    * All of mod projectile's inherited variables for constructors and update
    * public int direction = 1;
    * public int frame = 0;
    * public boolean alive;
    * public Hurtbox hurtbox;
    * public int damage;
    * public Vector velocity;
    * public double knockbackStrength;
    * public double dir;
    * public int width;
    * public int height;
    * public int life;
    * public int owner;
    * public int updatesPerTick = 1;
    * public Player ownerPlayer;
    * public boolean unreflectable = false;
    * public boolean draw = true;
    * public String fileName;
    * public BattleScreen battleScreen;
    */
   public SnowBall(double x, double y, double velX, double velY, double kbDir, int direction, int owner, Player ownerPlayer) {
      // the amount of damage the projectile does
      damage = 10;
      // width, must match the image
      width = 24;
      // height, must match the image
      height = 24;
      // how long the projectile lasts before despawning
      life = 45;
      // make it not die immediately
      alive = true;
      // the direction (left or right) of this projectile
      this.direction = direction;
      // how strong the knockback of this projectile is
      knockbackStrength = 3;
      // the id of the owner
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      // the direction (in radians) that the projectile sends the hit player
      dir = kbDir;
      // the image file name of the projectile's spritesheet
      fileName = "Snowball.png";

      // don't change these lines unless you know what you're doing (you probably
      // don't)
      pos = new Vector(x, y);
      hitbox = new AABB(x, y, width, height);
      velocity = new Vector(velX, velY);
   }

   boolean pastCollide = false;
   
   @Override
   public void update() {
      // gravity
      velocity.y += 2;
      // kill the projectile when its life reaches 0, if something else changes the
      // life variable you can use life <= 0 instead
      if (--life == 0) {
         alive = false;
      }

      // update hitbox before collision detection
      hitbox.setPosition(pos);
      
      if (velocity.y < 20 && velocity.y > -20) {
         frame = 0;
      } else {
         frame = 1;
      }

      boolean colliding = move(velocity, true);
      // this line of code checks to see if the projectile is overlapping the stage
      if (colliding) {
         velocity.y *= -1;
         velocity.y -= 2;
         frame = 2;
         if (pastCollide == true) {
            alive = false;
         } else {
            pastCollide = true;
         }
      } else {
         pastCollide = false;
      }
      
      // make the knockback direction rotate with the ball
      dir += Math.toRadians(2) * direction;
      // runs if hitting the opposite player
      hittingPlayer();
      // update the hurtbox for debug purposes
   }
   
   @Override
   public void onHitPlayer(Player player) {
      alive = false;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 2; i++) {
         battleScreen.addParticle(new GravityParticle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 1, 3, 3, "Snowmist.png", 1));
         battleScreen.addParticle(new GravityParticle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 1, 3, 3, "Snowmist_alt.png", 1));

      }
   }
}