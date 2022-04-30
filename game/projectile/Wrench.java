package game.projectile;

import game.Player;
import game.physics.*;
import java.util.List;
import game.particle.Particle;

public class Wrench extends Projectile {
   public Wrench(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 6;
      width = 48;
      this.direction = -direction;
      height = 48;
      life = 10;
      alive = true;
      knockbackStrength = 0;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "wrench.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   int frameTimer = 0;

   @Override
   public void update() {
      if (++frameTimer >= 8) {
         if (++frame >= 5) {
            frame = 1;
         }
         frameTimer = 0;
      }
      if (--life == 0) {
         alive = false;
      }
      for (Projectile projectile : ownerPlayer.battleScreen.getProjectiles()) {
         if (projectile.hitbox.overlaps(hitbox) && !projectile.unreflectable && projectile.ownerPlayer != ownerPlayer) {
            projectile.owner = owner;
            projectile.ownerPlayer = ownerPlayer;
            projectile.velocity.x *= -1;
            projectile.velocity.y *= -1;
            projectile.damage *= 2;
            projectile.dir = projectile.velocity.rotationOf();
            projectile.knockbackStrength *= 2;
            for (int i = 0; i < 4; i++) {
                  battleScreen.addParticle(new Particle(projectile.pos.x + projectile.width / 2,
                        projectile.pos.y + projectile.height / 2, (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, "twinkle.png"));
               }
         }
      }
      pos.x = ownerPlayer.pos.x + 6 + (36 * ownerPlayer.direction);
      pos.y = ownerPlayer.pos.y;
      if (frame == 1) {
         pos.y += 28;
      }
      pos.add(velocity);
      hitbox.setPosition(pos);
      List<Player> hitPlayers = hittingPlayer();
      if (hitPlayers != null) {
         for (Player player : hitPlayers) {
            if (player.playerId != owner) {
               player.hitPlayer(damage, 0, 0, 0, this, false);
               player.velocity = player.velocity.mul(-3);
               player.direction -= 1;
               for (int i = 0; i < 4; i++) {
                  battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
                        player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, "twinkle.png"));
               }
            }
            hitPlayer(player, 1);
         }
      }
   }

   @Override
   public boolean overrideHitPlayer() {
      return true;
   }

   @Override
   public void kill() {
   } // Unused
}