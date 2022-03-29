package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class ThrownAxe extends Projectile {

   Player originalPlayer;

   public ThrownAxe(double x, double y, double velX, double velY, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 42;
      width = 48;
      this.direction = direction;
      height = 48;
      alive = true;
      life = 40;
      knockbackStrength = 38;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      originalPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "axe.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
      hitPlayer = 0;
      unreflectable = false;
      ownerPlayer.selectedChar.hasKnife = false;
   }

   @Override
   public void update() {
      if (life % 2 == 0)
         if (++frame >= 8) {
            SoundEngine.playSound("swish");
            frame = 0;
         }

      boolean colliding = move(velocity, true);

      alive = !colliding;

      if (--life == 0) {
         alive = false;
      }

      if (life < 30) {
         velocity.y += 2;
      }

      hitbox.setPosition(pos);
      hittingPlayer();
      hitPlayer--;
   }

   @Override
   public void onHitPlayer(Player player) {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
               player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "blood.png"));
      }
      hitPlayer = 10;
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(center().x - 4, center().y - 4, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
      }
   }
}