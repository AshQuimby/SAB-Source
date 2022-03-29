package game.projectile;

import game.Player;

import game.physics.*;
import game.particle.Particle;

public class NoDuckSign extends Projectile {
   public NoDuckSign(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 4;
      width = 80;
      this.direction = direction;
      height = 64;
      life = 30;
      alive = true;
      knockbackStrength = 8;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      ownerPlayer.falling = true;
      dir = kbDir;
      fileName = "no_ducks.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if (--life == 0) {
         alive = false;
      }
      if (hitPlayer > 0)
         hitPlayer--;
      frame = 0;
      move(velocity, false);
      velocity.y += 0.2;
      boolean colliding = battleScreen.getStage().colliding(hitbox);
      if (colliding) {
         velocity.y = 0;
      }
      hitbox.setPosition(pos);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      hitPlayer = 4;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(pos.x + width / 2,
               pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
      }
   }
}
