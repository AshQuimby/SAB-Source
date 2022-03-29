package game.projectile;

import game.Player;

import game.physics.*;

public class Note extends Projectile {
   public Note(double x, double y, double velX, double velY, double kb, double kbDir, int owner, Player ownerPlayer) {
      damage = 4;
      width = 40;
      height = 40;
      life = 15;
      alive = true;
      knockbackStrength = 3;
      this.ownerPlayer = ownerPlayer;
      this.owner = owner;
      dir = kbDir;
      fileName = "note.png";
      pos = new Vector(x, y);
      unreflectable = false;
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      velocity.y += Math.cos(life / 1.5) * 4;
      if (life == 15)
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
      for (int i = 0; i < 4; i++) {
         Vector vel = new Vector((Math.random() - 0.5) * 10, (Math.random() - 0.5) * 10);
         battleScreen.addProjectile(new SmallNote(pos.x + width / 2, pos.y + height / 2, vel.x, vel.y, 1.2,
               vel.rotationOf(), ownerPlayer.keyLayout, ownerPlayer));
      }
   }
}