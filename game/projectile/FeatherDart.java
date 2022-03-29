package game.projectile;

import game.physics.*;
import game.particle.Particle;
import game.Player;

public class FeatherDart extends Projectile {
   public FeatherDart(double x, double y, double velX, double velY, int direction, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 6;
      this.knockbackStrength = 9;
      dir = kbDir;
      width = 56;
      height = 20;
      life = 12;
      alive = true;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      this.direction = direction;
      fileName = "feather_dart.png";
      velocity = new Vector(velX, velY);
      pos = new Vector(x, y);
      unreflectable = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.x *= 0.94;
      move(velocity, false);
      if (--life == 0) {
         alive = false;
      }
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      alive = false;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(pos.x + width / 2 - 2, pos.y + height / 2 - 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
      }
   }
}