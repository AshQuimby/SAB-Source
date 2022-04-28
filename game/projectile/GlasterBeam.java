package game.projectile;

import game.Player;
import game.physics.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import game.Images;

public class GlasterBeam extends Projectile {
   public GlasterBeam(double x, double y, int owner, int direction,
         Vector beamDirection, int charge, Player ownerPlayer) {
      width = 48;
      this.beamDirection = beamDirection;
      this.direction = direction;
      height = 48;
      alive = true;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      unreflectable = true;
      unParryable = true;
      knockbackStrength = charge + 8;
      damage = 12 + charge;
      dir = Math.toRadians(270);
      life = 5;
      fileName = "glaster_beam.png";
      pos = new Vector(x, y);
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   Vector beamDirection;

   @Override
   public void update() {
      if (life == 5) {
         hitbox.width = 464 * Math.abs(beamDirection.x) + 48;
         hitbox.height = 464 * Math.abs(beamDirection.y) + 48;
         if (beamDirection.x < 0) {
            hitbox.setX2(hitbox.x);
            hitbox.x += 48;
         }
         if (beamDirection.y < 0) {
            hitbox.setY2(hitbox.y);
            hitbox.y += 48;
         }
         hitbox.y -= 8;
         hitbox.x -= 4;
      }
      if (--life == 0) {
         alive = false;
      }

      hitbox.width -= 10 * Math.abs(beamDirection.y);
      hitbox.height -= 10 * Math.abs(beamDirection.x);
      hitbox.x += 5 * Math.abs(beamDirection.y);
      hitbox.y += 5 * Math.abs(beamDirection.x);
      dir = beamDirection.rotationOf();
      hittingPlayer();
   }

   @Override
   public void render(Graphics g, ImageObserver target) {
      if (life < 5) {
         BufferedImage image = Images.getImage(fileName);
         for (int i = 0; i < 512; i++) {
            Vector drawPos;
            if (beamDirection.x > 0 || beamDirection.y < 0) {
               drawPos = Vector.add(new Vector(hitbox.x, hitbox.y),
                     new Vector(beamDirection.x * i, beamDirection.y * i));
               drawPos.x -= 464 * Math.abs(beamDirection.x);
               drawPos.y += 464 * Math.abs(beamDirection.y);
            } else {
               drawPos = Vector.add(new Vector(hitbox.x, hitbox.y),
                     new Vector(beamDirection.x * i, beamDirection.y * i));
            }
            battleScreen.renderObject(g, image, drawPos, (int) hitbox.width, (int) hitbox.height, frame, false, target);
         }
      }
   }

   @Override
   public void onHitPlayer(Player player) {
      hitPlayer(player, 1);
   }

   @Override
   public void kill() {
   } // Unused
}