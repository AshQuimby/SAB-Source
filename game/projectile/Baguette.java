package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.particle.GravityParticle;
import game.SoundEngine;

public class Baguette extends Projectile {
   public Baguette(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 12;
      width = 64;
      this.direction = direction;
      height = 64;
      life = 12;
      alive = true;
      knockbackStrength = 12;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "baguette.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
      if (--life == 0) {
         if (ownerPlayer.readableKeys[Player.ATTACK]) {
            ownerPlayer.selectedChar.neutralAttack(ownerPlayer);
         }
         alive = false;
      }
      if (life < 10 && life > 2) {
         frame++;
      }
      pos.x = ownerPlayer.center().x - width / 2 + (32 * ownerPlayer.direction);
      pos.y = ownerPlayer.center().y - 48 + (13 - life) * 3;
      pos.add(velocity);
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
      SoundEngine.playSound("crunch");
      hitPlayer(player, 1);
   }

   @Override
   public void kill() {
      battleScreen.addParticle(new GravityParticle(hitbox.getCenterX() + 24, hitbox.getCenterY() + 24,
            (Math.random() - 0.5) * 4, (Math.random() - 3) * 4, 0.5, 8, 8, "block.png", 1));
   }
}