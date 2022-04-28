package game.projectile;

import game.physics.*;
import game.Player;

public class Banana extends Projectile {
   public Banana(double x, double y, int kb, int damage, int owner, Player ownerPlayer) {
      this.damage = damage;
      this.knockbackStrength = kb * 0.6;
      dir = 0;
      width = 128;
      height = 128;
      life = 8;
      alive = true;
      this.owner = owner;
      fileName = "banana.png";
      velocity = new Vector(0, 0);
      unreflectable = true;
      this.ownerPlayer = ownerPlayer;
      pos = new Vector(x, y);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.y -= 3;
      move(velocity, false);
      if (--life == 0) {
         alive = false;
      }
      if (life % 2 == 0) {
         if (++frame >= 4) {
            frame = 0;
         }
      }
      incrementHitPlayer(-1);
      hittingPlayer();
      hitbox.setPosition(pos);
   }

   @Override
   public void onHitPlayer(Player player) {
      dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
            .rotationOf();
      hitPlayer(player, 2);
   }

   @Override
   public void kill() {
   }
}