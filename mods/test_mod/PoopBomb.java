package mods.test_mod;

import game.physics.AABB;
import game.physics.Vector;
import modloader.ModProjectile;
import game.particle.GravityParticle;
import game.Player;

// this projectile is a projectile the player needs to charge to use
// for a simpler projectile check PoopBall.java

public class PoopBomb extends ModProjectile {
   int charge;

   public PoopBomb(double x, double y, double velX, double velY, double kb, double kbDir, int direction, int damage,
         int owner, Player ownerPlayer, int charge) {
      // we need to have custom damage and kb values for this as it varies based off
      // of the charge
      this.damage = damage;
      knockbackStrength = kb * 2;
      this.charge = charge;

      dir = kbDir;
      width = 36;
      height = 36;
      life = 45;
      alive = true;
      this.direction = direction;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      fileName = "poop_bomb.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      // change the velocity based on charge
      velocity.x *= ((this.charge / 5) + 20) / 6;
      velocity.x /= 2;
      hitbox = new AABB(x, y, width, height);
   }

   @Override
   public void update() {
      velocity.y += 2;
      if (++frame >= 4) {
         frame = 0;
      }
      if (--life <= 0) {
         alive = false;
      }
      boolean colliding = move(velocity, true);
      if (colliding) {
         life -= 4;
         velocity.y = 0.1;
      }
      // update hitbox before collision detection
      hitbox.setPosition(pos);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      alive = false;
      dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
            .rotationOf();
   }

   // makes poopy particles when the projectile dies
   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new GravityParticle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 6,
               (Math.random() - 0.5) * 8, 2, 3, 3, "poopy_particle.png", 1));
      }
   }
}