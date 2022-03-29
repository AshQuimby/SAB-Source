package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class ThrownDuckBomb extends Projectile {
   public ThrownDuckBomb(double x, double y, double velX, double velY, int damage, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      this.damage = damage;
      width = 44;
      height = 44;
      life = 120;
      alive = true;
      knockbackStrength = kb;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "duck_bomb.png";
      pos = new Vector(x, y);
      unreflectable = false;
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.y += 0.5;
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
      if (player.frozen > 1) {
         player.frozen = 1;
      }
      alive = false;
   }

   @Override
   public void kill() {
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
      width = 120;
      height = 120;
      pos.x -= 60;
      pos.y -= 60;
      hittingPlayer();
   }
}