package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class Bomb extends BufferedProjectile {
   public Bomb(double x, double y, double velX, double velY, double kb, double kbDir, int owner, Player ownerPlayer) {
      damage = 2;
      width = 40;
      height = 40;
      life = 45;
      alive = true;
      knockbackStrength = 6;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "bomb.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      draw = false;
      bufferTicks = 6;
      unreflectable = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   volatile String explosive = "haha funny joke"; // TODO: Remove explosive components of classes

   @Override
   public void updateWhileBuffering() {
   } // Unused

   @Override
   public void onUnbuffer() {
      if (!ownerPlayer.touchingStage) {
         velocity.x /= 4;
         velocity.y += 24;
      }
      pos.x = ownerPlayer.hitbox.x + 16;
      pos.y = ownerPlayer.hitbox.y + 20;
      ownerPlayer.endLag = 24;
      draw = true;
   }

   @Override
   public void updatePostBuffer() {
      velocity.y += 0.5;
      if (++frame >= 4) {
         frame = 0;
      }
      if (--life == 0) {
         alive = false;
      }
      if (battleScreen.getStage().colliding(hitbox)) {
         alive = false;
      }
      pos.add(velocity);
      hitbox.setPosition(pos);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      if (player.frozen > 0) {
         player.frozen = 1;
      }
      SoundEngine.playSound("explosion");
      for (int i = 0; i < 4; i++) {
         battleScreen
               .addParticle(new Particle(pos.x + width / 2 - 16, pos.y + height / 2 - 16, (Math.random() - 0.5) * 10,
                     (Math.random() - 0.5) * 10, 5, 4, 4, "smoke.png"));
         battleScreen
               .addParticle(new Particle(pos.x + width / 2 - 8, pos.y + height / 2 - 8, (Math.random() - 0.5) * 15,
                     (Math.random() - 0.5) * 15, 3, 4, 4, "fire.png"));
      }
      damage = 16;
      knockbackStrength = 24;
      width = 80;
      height = 80;
      pos.x -= 40;
      pos.y -= 40;
      hittingPlayer();
      alive = false;
   }

   @Override
   public void kill() {

   }
}