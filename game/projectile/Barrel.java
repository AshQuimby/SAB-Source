package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.*;
import game.SoundEngine;

public class Barrel extends Projectile {
   public Barrel(int owner, Player ownerPlayer) {
      life = 16;
      alive = true;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      fileName = "none.png";
      unreflectable = true;
      draw = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      ownerPlayer.endLag = 1;
      ownerPlayer.frame = 10;
      ownerPlayer.velocity.y -= 5;
      if (ownerPlayer.tookDamage) {
         alive = false;
      }
      if (--life == 0) {
         alive = false;
      }
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen
               .addParticle(new Particle(ownerPlayer.pos.x + ownerPlayer.width / 2 - 16,
                     ownerPlayer.pos.y + ownerPlayer.height / 2 - 16, (Math.random() - 0.5) * 10,
                     (Math.random() - 0.5) * 10, 5, 4, 4, "smoke.png"));
         battleScreen
               .addParticle(new Particle(ownerPlayer.pos.x + ownerPlayer.width / 2 - 8,
                     ownerPlayer.pos.y + ownerPlayer.height / 2 - 8, (Math.random() - 0.5) * 15,
                     (Math.random() - 0.5) * 15, 3, 4, 4, "fire.png"));
      }
      battleScreen.addParticle(
            new GravityParticle(ownerPlayer.pos.x + 64, ownerPlayer.pos.y + 52,
                  (Math.random() - 0.5) * 5, (Math.random() - 1) * 8, 1, 16, 20, "barrel.png", 1));
      SoundEngine.playSound("explosion");
      ownerPlayer.touchingStage = false;
      ownerPlayer.velocity.y = -92;
      ownerPlayer.falling = true;
   }
}