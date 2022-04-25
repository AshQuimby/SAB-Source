package game.projectile;

import game.Player;
import game.physics.*;
import game.SoundEngine;
import game.particle.Particle;

public class JohnSuck extends PersistingProjectile {
   public JohnSuck(double x, double y, int direction, int owner, Player ownerPlayer) {
      width = 80;
      height = 80;
      alive = true;
      life = 6;
      this.ownerPlayer = ownerPlayer;
      this.owner = owner;
      this.direction = direction;
      fileName = "john_suck.png";
      unreflectable = true;
      pos = new Vector(x, y);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   int frameTimer = 0;
   int trapPlayerTimer = 0;
   Player trappedPlayer;

   @Override
   public void updateWhilePersisting() {
      if (ownerPlayer.knockBack.len() > 2) {
         alive = false;
      }

      life--;
      if (trapPlayerTimer <= 0) {
         ownerPlayer.endLag = 8;
         if (life % 4 == 0) {
            ownerPlayer.frame = 11;
         } else if ((life + 2) % 4 == 0) {
            ownerPlayer.frame = 12;
         }
         if (frameTimer++ >= 2) {
            if (frame++ >= 5) {
               frame = 0;
            }
            SoundEngine.playSound("swish");
            frameTimer = 0;
         }
         Vector vel = new Vector(10, 0).rotateBy(Vector.sub(center(), ownerPlayer.center()).rotationOf());
         vel = vel.mul(-1);
         battleScreen.addParticle(new Particle(pos.x + (width / 2 * (Math.random() - 0.5)) + 74,
               pos.y + (height / 2 * (Math.random() - 0.5)) + 52, vel.x * Math.random(),
               vel.y * Math.random(), 1, 5, 5, "smoke_p0.png"));
         pos = Vector.sub(
               Vector.add(ownerPlayer.pos, new Vector(ownerPlayer.hitbox.width / 2, ownerPlayer.hitbox.height / 2)),
               new Vector(width / 2, height / 2));
         pos.add(new Vector(44 * ownerPlayer.direction, 0));
         hitbox.setPosition(pos);
         if (hittingPlayer() != null)
            for (Player player : hittingPlayer()) {
               if (player.playerId != owner) {
                  player.velocity.x = 0;
                  player.velocity.y = 0;
                  player.hitbox.x = 0;
                  for (int i = 0; i < 4; i++) {
                     vel = new Vector(10, 0).rotateBy(Vector.sub(center(), ownerPlayer.center()).rotationOf());
                     vel = vel.mul(-1);
                     battleScreen.addParticle(new Particle(pos.x + width / 2, pos.y + height / 2, vel.x * Math.random(),
                           vel.y * Math.random(), 3, 5, 5, "smoke.png"));
                  }
                  for (int i = 0; i < 4; i++) {
                     battleScreen.addParticle(
                           new Particle(player.pos.x + width / 2, player.pos.y + player.height / 2,
                                 (Math.random() - 0.5) * 3,
                                 (Math.random() - 0.5) * 3, 3, 5, 5, "smoke.png"));
                  }
                  trappedPlayer = player;
                  trappedPlayer.invincible = true;
                  trappedPlayer.render = false;
                  trappedPlayer.hitbox.setCenter(ownerPlayer.center());
                  trapPlayerTimer = 1;
                  trappedPlayer.endLag = 10;
                  player.knockBack = player.knockBack.mul(0);
               }
            }
      } else {
         draw = false;
         trapPlayerTimer++;
         ownerPlayer.pos.add(new Vector((Math.random() - 0.5) * 4, (Math.random() - 0.5) * 4));
         direction = ownerPlayer.direction;
         ownerPlayer.velocity.x *= 0.96;
         trappedPlayer.endLag = 10;
         trappedPlayer.stunned = 2;
         trappedPlayer.hitbox
               .setCenter(Vector.sub(ownerPlayer.center(), new Vector(0, 48)));
         direction = ownerPlayer.direction;
         for (int t : trappedPlayer.readableKeysJustPressed) {
            if (t == 1) {
               trapPlayerTimer += Math.ceil(10.0 / (trappedPlayer.damage + 3));
            }
         }
         if (trapPlayerTimer >= 50) {
            if (direction == 1) {
               dir = Math.toRadians(320);
            } else {
               dir = Math.toRadians(220);
            }
            ownerPlayer.endLag = 8;
            trappedPlayer.render = true;
            trapPlayerTimer = -1;
            alive = false;
         }
      }
      ownerPlayer.velocity.y -= 2;
   }

   @Override
   public boolean drawPriority() {
      return true;
   }

   @Override
   public boolean overrideHitPlayer() {
      return true;
   }

   @Override
   public void kill() {
      ownerPlayer.endLag = 8;
      if (trappedPlayer != null && trapPlayerTimer < 120) {
         if (direction == 1) {
            dir = Math.toRadians(320);
         } else {
            dir = Math.toRadians(220);
         }
         ownerPlayer.frame = 12;
         trappedPlayer.hitPlayer(6, 24, direction == -1 ? Math.toRadians(180) : Math.toRadians(0), 0.01, this);
         trappedPlayer.render = true;
         alive = false;
      }
   }
}