package game.projectile;

import game.Player;
import game.physics.*;
import game.SoundEngine;
import game.particle.Particle;

public class Suck extends PersistingProjectile {
   public Suck(double x, double y, int direction, int owner, Player ownerPlayer) {
      damage = 4;
      width = 116;
      height = 108;
      alive = true;
      life = 6;
      this.ownerPlayer = ownerPlayer;
      this.owner = owner;
      this.direction = direction;
      fileName = "suck.png";
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
      if (life == 6) {
         ownerPlayer.frame = 4;
      }
      life--;
      if (trapPlayerTimer <= 0) {
         if (frameTimer++ >= 2) {
            if (frame++ >= 7) {
               frame = 0;
            }
            SoundEngine.playSound("swish");
            frameTimer = 0;
         }
         Vector vel = new Vector(10, 0).rotateBy(Vector.sub(center(), ownerPlayer.center()).rotationOf());
         vel = vel.mul(-1);
         battleScreen.addParticle(new Particle(pos.x + (width / 2 * (Math.random() - 0.5)) + 64,
               pos.y + (height / 2 * (Math.random() - 0.5)) + 56, vel.x * Math.random(),
               vel.y * Math.random(), 1, 5, 5, "smoke.png"));
         ownerPlayer.endLag = 1;
         pos = Vector.sub(
               Vector.add(ownerPlayer.pos, new Vector(ownerPlayer.hitbox.width / 2, ownerPlayer.hitbox.height / 2)),
               new Vector(width / 2, height / 2));
         pos.add(new Vector(96 * ownerPlayer.direction, -10));
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
                  player.endLag = 10;
                  trappedPlayer = player;
                  trappedPlayer.invincible = true;
                  trappedPlayer.render = false;
                  trappedPlayer.hitbox.setCenter(ownerPlayer.center());
                  trapPlayerTimer = 1;
                  player.knockBack = player.knockBack.mul(0);
               }
            }
      } else {
         draw = false;
         trapPlayerTimer++;
         if (trapPlayerTimer % 6 == 0) {
            trappedPlayer.damage += 8;
            ownerPlayer.frame = 4;
            SoundEngine.playSound("crunch");
            SoundEngine.playSound("hit");
         }
         if ((trapPlayerTimer + 3) % 6 == 0) {
            ownerPlayer.frame = 5;
         }
         if (trapPlayerTimer >= 20) {
            if (direction == 1) {
               dir = Math.toRadians(320);
            } else {
               dir = Math.toRadians(220);
            }
            trappedPlayer.hitbox
                  .setPosition(new Vector(pos.x + width / 2 - trappedPlayer.hitbox.width / 2 - 30 * direction,
                        pos.y + height / 2 - trappedPlayer.hitbox.height / 2));
            trappedPlayer.hitPlayer(12, 42, dir, 0.05, this);
            trappedPlayer.render = true;
            alive = false;
         }
      }
      ownerPlayer.velocity.y -= 2;
   }

   @Override
   public boolean overrideHitPlayer() {
      return true;
   }

   @Override
   public void kill() {
      if (trappedPlayer != null) {
         if (direction == 1) {
            dir = Math.toRadians(320);
         } else {
            dir = Math.toRadians(220);
         }
         trappedPlayer.hitbox
               .setPosition(new Vector(pos.x + width / 2 - trappedPlayer.hitbox.width / 2 - 30 * direction,
                     pos.y + height / 2 - trappedPlayer.hitbox.height / 2));
         trappedPlayer.hitPlayer(12, 32, dir, 0.01, this);
         trappedPlayer.render = true;
         alive = false;
      }
   }
}