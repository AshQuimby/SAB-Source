package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class Slash extends Projectile {
   public Slash(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 12;
      width = 48;
      this.direction = direction;
      height = 48;
      life = 5;
      alive = true;
      knockbackStrength = 12;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "slash.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if (--life == 0) {
         alive = false;
      }
      if (ownerPlayer.touchingStage) {
         damage = 12;
      } else {
         damage = 16;
      }
      pos.x = ownerPlayer.hitbox.x - 2 + (36 * ownerPlayer.direction);
      pos.y = ownerPlayer.hitbox.y;
      pos.add(velocity);
      hitbox.setPosition(pos);
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
               player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "blood.png"));
      }
      hitPlayer = 1;
   }

   @Override
   public void kill() {
   } // Unused
}