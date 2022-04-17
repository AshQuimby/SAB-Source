package mods.test_mod;

import game.physics.AABB;
import game.physics.Vector;
import modloader.ModProjectile;
import game.particle.GravityParticle;
import game.Player;
import game.SoundEngine;

// this projectile is a projectile the player needs to charge to use
// for a simpler projectile check PoopBall.java

public class SludgeBomb extends ModProjectile {

   public SludgeBomb(Player ownerPlayer) {
      // we need to have custom damage and kb values for this as it varies based off
      // of the charge
      this.damage = 32;
      knockbackStrength = 32;
      dir = 0;
      width = 320;
      height = 320;
      life = 80;
      alive = true;
      this.direction = ownerPlayer.direction;
      this.owner = ownerPlayer.keyLayout;
      this.ownerPlayer = ownerPlayer;
      fileName = "sludge_bomb.png";
      pos = new Vector(ownerPlayer.center().x, -300);
      velocity = new Vector(0, 0);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.y += 2;
      if (move(velocity, true)) {
         SoundEngine.playSound("explosion");
         SoundEngine.playSound("fart");
         velocity.y *= -0.95;
         velocity.x += (Math.random() - 0.5) * 4;
      }
      hitPlayer--;
      // update hitbox before collision detection
      hitbox.setPosition(pos);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      hitPlayer = 10;
      dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), new Vector(center().x, hitbox.getY2())).rotationOf())
            .rotationOf();
   }

   // makes poopy particles when the projectile dies
   @Override
   public void kill() {
      for (int i = 0; i < 20; i++) {
         battleScreen.addParticle(new GravityParticle(hitbox.getRandomPoint().x, hitbox.getRandomPoint().y, (Math.random() - 0.5) * 6,
               (Math.random() - 0.5) * 8, 4, 3, 3, "poopy_particle.png", 1));
      }
   }
}