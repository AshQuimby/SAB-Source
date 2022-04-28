package game.projectile;

import game.physics.*;
import game.particle.Particle;
import game.Player;

public class WindBlast extends Projectile {
   public WindBlast(double x, double y, int direction, double kbDir, int owner, Player ownerPlayer) {
      damage = 0;
      this.knockbackStrength = 36;
      dir = kbDir;
      width = 120;
      height = 116;
      life = 8;
      alive = true;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      this.direction = direction;
      fileName = "wind_blast.png";
      velocity = new Vector(0, 0);
      velocity.x = 14 * direction;
      unreflectable = false;
      pos = new Vector(x, y);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if ((life + 1) % 2 == 0) {
         frame++;
      }
      move(velocity, false);
      if (--life == 0) {
         alive = false;
      }
      hittingPlayer();
      hitbox.setPosition(pos);
      
      for (Projectile projectile : ownerPlayer.battleScreen.getProjectiles()) {
         if (projectile.hitbox.overlaps(hitbox) && !projectile.unreflectable && projectile.ownerPlayer != ownerPlayer) {
            projectile.owner = owner;
            projectile.ownerPlayer = ownerPlayer;
            for (int i = 0; i < 4; i++) {
                  battleScreen.addParticle(new Particle(projectile.pos.x + projectile.width / 2,
                        projectile.pos.y + projectile.height / 2, (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, "twinkle.png"));
               }
         }
         if (projectile.hitbox.overlaps(hitbox) && !projectile.unreflectable && projectile != this) {
             projectile.dir = projectile.velocity.rotationOf();
             projectile.velocity.x += 10 * direction;
         }
      }
   }

   @Override
   public void onHitPlayer(Player player) {
      hitPlayer(player, 1);
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(pos.x + width / 2 - 2, pos.y + height / 2 - 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
      }
   }
}