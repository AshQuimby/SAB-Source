package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class BlockSmash extends Projectile {
   public BlockSmash(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 16;
      width = 48;
      height = 48;
      life = 4;
      alive = true;
      knockbackStrength = 38;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "none.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      draw = false;
      unreflectable = true;
      hitbox = new AABB(pos.x - 24, pos.y - 16 - 24, width, height);
   }

   @Override
   public void update() {
      velocity.y += 0.3;
      if (--life == 0) {
         alive = false;
      }
      pos.add(velocity);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
               player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "blood.png"));
      }
      alive = false;
   }

   @Override
   public void kill() {
   }
}