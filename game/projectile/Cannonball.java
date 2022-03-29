package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class Cannonball extends BufferedProjectile {
   public Cannonball(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 20;
      width = 32;
      height = 32;
      life = 26;
      alive = true;
      knockbackStrength = 8;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      bufferTicks = 10;
      draw = false;
      fileName = "cannonball.png";
      pos = new Vector(x, y);
      unreflectable = false;
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void updateWhileBuffering() {
      ownerPlayer.velocity.y -= 2;
      ownerPlayer.endLag = 1;
      ownerPlayer.frame = 9;
   }

   @Override
   public void onUnbuffer() {
      if (ownerPlayer.readableKeys[Player.ATTACK]) {
         ownerPlayer.battleScreen.addProjectileAtCenter(
               new Suck(ownerPlayer.center().x, ownerPlayer.center().y, ownerPlayer.direction, owner, ownerPlayer));
      }
      draw = true;
      ownerPlayer.endLag = 15;
      SoundEngine.playSound("gunshot");
      pos.x = ownerPlayer.pos.x + 16;
      pos.y = ownerPlayer.pos.y + 28;
      ownerPlayer.move(new Vector(-12 * ownerPlayer.direction, 0), true);
      draw = true;
      ownerPlayer.frame = 4;
      if (ownerPlayer.touchingStage)
         damage += 8;
      battleScreen
            .addParticle(
                  new Particle(pos.x + 12 + (32 * ownerPlayer.direction), pos.y + 6, 0, 0, 2, 8, 8, "twinkle.png"));
      battleScreen
            .addParticle(new Particle(pos.x + 12 + (32 * ownerPlayer.direction), pos.y + 6, (Math.random() - 0.5) * 4,
                  (Math.random() - 0.5) * 4, 2, 4, 4, "fire.png"));
      battleScreen.addParticle(new Particle(pos.x + 12 + (32 * ownerPlayer.direction), pos.y + 6,
            (Math.random() - 0.5) * 4, (Math.random() - 1) * 4, 1.5, 6, 4, "smoke.png"));
   }

   @Override
   public void updatePostBuffer() {
      ownerPlayer.velocity.y -= 2;
      if (life < 10) {
         velocity.x *= 0.95;
         velocity.y += 3;
      }
      if (--life == 0) {
         alive = false;
      }
      move(velocity, true);
      hitbox.setPosition(pos);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      alive = false;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 3, 5, 5, "smoke.png"));
      }
   }
}