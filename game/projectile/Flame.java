package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.SpecialFire;

public class Flame extends Projectile {
   public Flame(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 6;
      width = 256;
      height = 48;
      life = 2;
      alive = true;
      knockbackStrength = 6;
      this.owner = owner;
      dir = kbDir;
      this.ownerPlayer = ownerPlayer;
      fileName = "none.png";
      pos = new Vector(x, y);
      unreflectable = false;
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if (++frame >= 4) {
         frame = 0;
      }
      if (--life == 0) {
         alive = false;
      }
      for (int i = 0; i < 4; i++) {
         Vector fireSpew = new Vector(14 * ownerPlayer.direction, 0).rotateBy((Math.random() - 0.5) * 0.2)
               .mul((Math.random() + 0.5));
         battleScreen
               .addParticle(
                     new SpecialFire(center().x + 6, center().y - 4, fireSpew.x, fireSpew.y, Math.random() + 0.5, 5,
                           5,
                           "fire.png", 0.1f));
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

      }
   }
}