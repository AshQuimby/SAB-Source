package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class Fireball extends Projectile {
   public Fireball(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 12;
      width = 32;
      height = 32;
      life = 30;
      alive = true;
      knockbackStrength = 8;
      this.owner = owner;
      dir = kbDir;
      this.ownerPlayer = ownerPlayer;
      fileName = "fireball.png";
      pos = new Vector(x, y);
      unreflectable = false;
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.y += Math.sin(life / 4.0);
      if (++frame >= 4) {
         frame = 0;
      }
      if (--life == 0) {
         alive = false;
      }
      pos.add(velocity);
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
               (Math.random() - 0.5) * 3, 3, 5, 5, "fire.png"));
      }
   }
}