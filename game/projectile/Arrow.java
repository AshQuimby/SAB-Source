package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class Arrow extends BufferedProjectile {
   public Arrow(double x, double y, double velX, double velY, double kb, double kbDir, int owner, Player ownerPlayer) {
      damage = 16;
      width = 28;
      height = 12;
      life = 30;
      alive = true;
      knockbackStrength = 6;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "arrow.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      draw = false;
      bufferTicks = 6;
      unreflectable = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void updateWhileBuffering() {
      ownerPlayer.frame = 9;
   }

   @Override
   public void onUnbuffer() {
      this.direction = ownerPlayer.direction;
      ownerPlayer.frame++;
      pos.x = ownerPlayer.hitbox.x + 16;
      pos.y = ownerPlayer.hitbox.y + 20;
      ownerPlayer.endLag = 8;
      draw = true;
   }

   @Override
   public void updatePostBuffer() {
      velocity.y += 0.3;
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
      alive = false;
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
               player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "blood.png"));
      }
   }

   @Override
   public void kill() {
   }
}