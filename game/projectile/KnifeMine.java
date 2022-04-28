package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class KnifeMine extends Projectile {
   public KnifeMine(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 12;
      width = 16;
      this.direction = -direction;
      height = 56;
      life = 120;
      alive = true;
      knockbackStrength = 6;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "knife_spike.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.y += 3;
      if (--life <= 0) {
         for (int i = 0; i < 4; i++) {
            battleScreen.addParticle(new Particle(pos.x + 10, pos.y + 10, (Math.random() - 0.5) * 3,
                  (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
         }
         alive = false;
      }
      move(velocity, false);
      if (battleScreen.getStage().colliding(hitbox)) {
         velocity.y = -3;
         frame = 0;
         knockbackStrength = 12;
         dir = Math.toRadians(270);
         damage = 8;
      } else {
         frame = 1;
         knockbackStrength = 24;
         dir = Math.toRadians(90);
         damage = 14;
      }
      hitbox.setPosition(pos);
      incrementHitPlayer(-1);
      for (int i = 0; i < battleScreen.getProjectiles().size(); i++) {
         if (battleScreen.getProjectiles().get(i) != this
               && battleScreen.getProjectiles().get(i).getClass() == this.getClass()
               && battleScreen.getProjectiles().get(i).life < life
               && battleScreen.getProjectiles().get(i).owner == this.owner) {
            battleScreen.getProjectiles().get(i).alive = false;
            Projectile proj = battleScreen.getProjectiles().get(i);
            for (int j = 0; j < 4; j++) {
               battleScreen.addParticle(new Particle(proj.pos.x + 10, proj.pos.y + 10, (Math.random() - 0.5) * 3,
                     (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
            }
         }
      }
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      hitPlayer(player, 20);
      life -= 9;
   }

   @Override
   public void kill() {
   } // Unused
}