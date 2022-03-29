package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class Frostball extends Projectile {
   public Frostball(double x, double y, double velX, double velY, int damage, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      this.damage = damage;
      width = 32;
      height = 32;
      life = 120;
      alive = true;
      knockbackStrength = kb * 1.5;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "frostball.png";
      pos = new Vector(x, y);
      unreflectable = false;
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.y += Math.sin(life * 4) * 2 + 0.3;
      if (++frame >= 4) {
         frame = 0;
      }
      if (--life == 0) {
         alive = false;
      }
      boolean colliding = move(velocity, true);
      hitbox.setPosition(pos);
      if (colliding) {
         alive = false;
      }
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      if (player.frozen == 0 && player.damage + damage > 40) {
         player.frozen = player.damage;
      } else if (player.frozen > 0) {
         player.frozen = 1;
      }
      alive = false;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 3, 5, 5, "ice_shard.png"));
      }
   }
}