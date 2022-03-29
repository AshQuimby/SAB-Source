package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class SmallNote extends Projectile {
   public SmallNote(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
         Player ownerPlayer) {
      damage = 1;
      width = 24;
      height = 24;
      life = 10;
      alive = true;
      knockbackStrength = 0;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "small_note.png";
      unreflectable = false;
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if (life == 10)
         frame = (int) Math.round(Math.random() * 2);
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
      battleScreen.addParticle(new Particle(pos.x + width / 2, pos.y + height / 2, (Math.random() - 0.5) * 3,
            (Math.random() - 0.5) * 3, 1, 5, 5, "note_particle.png"));
   }
}