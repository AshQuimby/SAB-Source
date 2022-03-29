package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class SussyVent extends Projectile {
   public SussyVent(double x, double y, int owner, Player ownerPlayer) {
      width = 64;
      this.direction = -direction;
      height = 96;
      life = 60;
      alive = true;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      fileName = "vent.png";
      pos = new Vector(x, y);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, 64);
      hitboxOffset = new Vector(0, 48);
      frame = 0;
   }

   int frameTimer = 0;

   @Override
   public void update() {
      ownerPlayer.frame = 6;
      ownerPlayer.velocity.x = 0;
      ownerPlayer.velocity.y = 0;
      ownerPlayer.falling = true;
      if (life > 50) {
         if (life == 58)
            frame++;
         if (life == 56)
            frame++;
         if (life == 54)
            frame++;
         if (life == 51)
            frame = 0;
         ownerPlayer.render = true;
      } else if (life < 5) {
         ownerPlayer.render = true;
         if (life == 4)
            frame++;
         if (life == 1)
            frame = 0;
      } else {
         ownerPlayer.falling = true;
         ownerPlayer.hitbox.x = 0;
         hitbox.setPosition(pos);
         hitbox.y += 48;
         if (ownerPlayer.readableKeys[Player.UP]) {
            move(new Vector(0, -6), true);
         }
         if (ownerPlayer.readableKeys[Player.DOWN]) {
            move(new Vector(0, 6), true);
         }
         if (ownerPlayer.readableKeys[Player.LEFT]) {
            move(new Vector(-6, 0), true);
         }
         if (ownerPlayer.readableKeys[Player.RIGHT]) {
            move(new Vector(6, 0), true);
         }
         ownerPlayer.invincible = true;
         if (ownerPlayer.readableKeysJustPressed[Player.ATTACK] == 1) {
            life = 4;
         }
         if (life == 10)
            frame++;
         if (life == 8)
            frame++;
      }
      if (life >= 5 && life <= 50)
         ownerPlayer.render = false;
      if (--life == 0) {
         ownerPlayer.velocity.y = -10;
         alive = false;
      }
      if (ownerPlayer.tookDamage) {
         alive = false;
         ownerPlayer.render = true;
         ownerPlayer.damage += 12;
      }
      ownerPlayer.hitbox.x = pos.x + 16;
      ownerPlayer.hitbox.y = pos.y + 16;
      if (pos.y > ownerPlayer.battleScreen.getStage().getSafeBlastZone().getY2()) {
         pos.y = ownerPlayer.battleScreen.getStage().getSafeBlastZone().getY2();
         if (life > 9)
            life = 9;
      }
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(pos.x + width / 2, pos.y + width / 2 + 10, (Math.random() - 0.5) * 5,
               (Math.random() - 0.5) * 5, 3, 4, 4, "smoke.png"));
      }
   }
}