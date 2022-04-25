package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;
import game.SoundEngine;

import java.util.List;

public class AirSlash extends Projectile {
   public AirSlash(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 2;
      width = 120;
      height = 64;
      life = 20;
      alive = true;
      knockbackStrength = 10;
      this.owner = owner;
      this.direction = direction;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "air_slash.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   int frameTimer = 0;

   @Override
   public void update() {
      if (life == 20)
         ownerPlayer.velocity.y = 0;
      if (++frameTimer >= 1) {
         if (++frame >= 5) {
            frame = 0;
         }
         frameTimer = 0;
      }
      if (frame == 1)
         SoundEngine.playSound("swish");
      if (ownerPlayer.tookDamage)
         alive = false;
      if (ownerPlayer.readableKeysJustPressed[5] == 1) {
         ownerPlayer.velocity.y -= 1.2;
         SoundEngine.playSound("swish");
      }
      direction = -direction;
      ownerPlayer.direction = direction;
      ownerPlayer.frame = 6;
      pos.x = ownerPlayer.hitbox.x - 36;
      pos.y = ownerPlayer.hitbox.y;
      pos.add(velocity);
      hitbox.setPosition(pos);
      hitPlayer--;
      ownerPlayer.velocity.x *= 0.8;
      ownerPlayer.velocity.y -= 3;
      knockbackStrength = 0;
      double mult = 0.0;
      if (ownerPlayer.readableKeys[Player.DOWN]) {
         ownerPlayer.velocity.y += 2;
      }
      if (--life == 0)
         alive = false;
      for (int i = 0; i < battleScreen.getProjectiles().size(); i++) {
         if (battleScreen.getProjectiles().get(i) != this
               && battleScreen.getProjectiles().get(i).getClass() == this.getClass()
               && battleScreen.getProjectiles().get(i).life < life
               && battleScreen.getProjectiles().get(i).owner == this.owner) {
            battleScreen.getProjectiles().get(i).alive = false;
            Projectile proj = battleScreen.getProjectiles().get(i);
            for (int j = 0; j < 4; j++) {
               battleScreen.addParticle(
                     new Particle(proj.pos.x + width / 2, proj.pos.y + height / 2, (Math.random() - 0.5) * 3,
                           (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
            }
         }
      }
      if (ownerPlayer.touchingStage)
         alive = false;
      if (life <= 1 && hittingPlayer() != null) {
         knockbackStrength = 36;
         dir += (Math.random() - 0.5) / 2;
         mult = 0.067;
      }
      List<Player> hitPlayers = hittingPlayer();
      if (hitPlayers != null) {
         for (Player player : hitPlayers) {
            if (player.playerId != owner) {
               for (int i = 0; i < 4; i++) {
                  battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
                        player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, "blood.png"));
               }
               hitPlayer = 2;
               player.move(new Vector(48, 0)
                     .rotateBy((Vector.sub(ownerPlayer.hitbox.getCenter(), player.hitbox.getCenter()).rotationOf())),
                     true);
               player.hitPlayer(damage, 10, dir, mult, this);
               player.velocity.y *= 0.2;
               player.velocity.x *= 0.3;
            }
         }
      }
   }

   @Override
   public boolean overrideHitPlayer() {
      return true;
   }

   @Override
   public void kill() {
   } // Unused
}