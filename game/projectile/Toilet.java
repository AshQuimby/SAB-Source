package game.projectile;

import game.Player;

import game.physics.*;
import game.particle.Particle;

public class Toilet extends Projectile {

   Player originalPlayer;
   
   public Toilet(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 6;
      width = 40;
      this.direction = direction;
      height = 80;
      life = 30;
      alive = true;
      knockbackStrength = 6;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      originalPlayer = ownerPlayer;
      ownerPlayer.falling = true;
      dir = kbDir;
      fileName = "toilet.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if (--life < 0) {
         for (int i = 0; i < 4; i++) {
            battleScreen.addParticle(new Particle(pos.x + 10, pos.y + 10, (Math.random() - 0.5) * 3,
                  (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
            alive = false;
         }
      }
      frame = 0;
      boolean colliding = move(velocity, true);
      velocity.y += 0.1;
      hitbox.setPosition(pos);
      if (colliding) {
      } else if (life < 10 && pos.y < 560) {
         life += 4;
      }
      if (life <= 15) {
         velocity.y += 0.3;
         damage = 12;
         dir = Math.toRadians(90);
         knockbackStrength = 20;
         frame = 1;
         if (life % 4 == 0) {
            battleScreen.addParticle(new Particle(pos.x + 20, pos.y + 10, (Math.random() - 0.5) * 3,
                  (Math.random() + 0.3) * -20, 2, 3, 6, "water.png"));
            battleScreen.addProjectile(
                  new Water(pos.x, pos.y - 60, 10.0, Math.toRadians(270), ownerPlayer.keyLayout, ownerPlayer));
         }
         if (life == 15) {
            originalPlayer.velocity.y = -50;
         }
      } else {
         originalPlayer.hitbox.x = pos.x - 4;
         originalPlayer.hitbox.y = pos.y - 15;
         originalPlayer.falling = true;
         originalPlayer.velocity.x = 0;
         originalPlayer.velocity.y = 0;
         originalPlayer.frame = 6;
      }
      hittingPlayer();
   }

   @Override
   public void onHitPlayer(Player player) {
      hitPlayer = 1;
   }

   @Override
   public void kill() {
   } // Unused
}