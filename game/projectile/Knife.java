package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

public class Knife extends Projectile {
   
   Player originalPlayer;
      
   public Knife(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 10;
      width = 40;
      this.direction = direction;
      height = 40;
      alive = true;
      life = 600;
      knockbackStrength = 24;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      originalPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "knife.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      hitbox = new AABB(pos.x, pos.y, width, height);
      hitPlayer = 0;
      unreflectable = false;
      ownerPlayer.selectedChar.hasKnife = false;
   }

   @Override
   public void update() {
      if (++frame >= 8) {
         SoundEngine.playSound("swish");
         frame = 0;
      }
      if (--life < 0) {
         ownerPlayer.selectedChar.hasKnife = true;
         alive = false;
      }
      if (life == 570) {
         if (dir == Math.toRadians(235))
            dir = Math.toRadians(325);
         else if (dir == Math.toRadians(305))
            dir = Math.toRadians(215);
         hitPlayer = 0;
      }
      if (life <= 570) {
         velocity = Vector.normalize(Vector.sub(ownerPlayer.pos, pos)).mul(20);
         AABB collider1 = new AABB(pos.x, pos.y, width, height);
         AABB collider2 = new AABB(ownerPlayer.pos.x, ownerPlayer.pos.y, ownerPlayer.width, ownerPlayer.height);
         if (collider1.overlaps(collider2)) {
            alive = false;
         }
      }
      pos.add(velocity);
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
      originalPlayer.selectedChar.hasKnife = true;
   }
}