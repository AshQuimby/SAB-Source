package game.projectile.final_asses;

import game.Player;
import game.physics.*;
import game.projectile.Projectile;
import game.particle.Particle;

public class Bong extends Projectile {
   public Bong(double x, double y, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 24;
      width = 96;
      this.direction = direction;
      height = 96;
      life = 7;
      alive = true;
      knockbackStrength = 24;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "bell_dong.png";
      pos = new Vector(x, y);
      velocity = new Vector(0, 0);
      unreflectable = true;
      unParryable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      frame++;
      if (--life == 0) {
         alive = false;
      }
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
      dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
                .rotationOf();
      hitPlayer(player, 1);
   }

   @Override
   public void kill() {
   }
}