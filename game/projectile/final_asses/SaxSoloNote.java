package game.projectile.final_asses;

import game.Player;
import game.physics.*;
import game.projectile.Projectile;

public class SaxSoloNote extends Projectile {
   public SaxSoloNote(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 8;
      width = 40;
      height = 40;
      life = 120;
      alive = true;
      knockbackStrength = 3;
      this.ownerPlayer = ownerPlayer;
      this.owner = owner;
      unreflectable = true;
      unParryable = true;
      dir = kbDir;
      fileName = "note.png";
      pos = new Vector(x, y);
      unreflectable = false;
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.y += Math.sin(life / 1.5) * 4;
      if (life == 120)
         frame = (int) Math.round(Math.random() * 3);
      if (--life == 0) {
         alive = false;
      }
      if (life % 8 == 0)
         direction = -direction;
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
   }
}