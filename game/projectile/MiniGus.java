package game.projectile;

import game.Player;

import game.physics.*;
import game.particle.Particle;

public class MiniGus extends HomingProjectile {
   public MiniGus(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
         Player ownerPlayer) {
      damage = 6;
      width = 32;
      this.direction = direction;
      height = 40;
      life = 120;
      alive = true;
      knockbackStrength = 6;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = kbDir;
      fileName = "mini_gus.png";
      pos = new Vector(x, y);
      velocity = new Vector(velX, velY);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   boolean moving;
   int jumpCooldown = 0;

   @Override
   public void updateAfterTargeting() {
      if (life > 15) {
         for (int i = 0; i < battleScreen.getProjectiles().size(); i++) {
            if (battleScreen.getProjectiles().get(i) != this
                  && battleScreen.getProjectiles().get(i).getClass() == this.getClass()
                  && battleScreen.getProjectiles().get(i).life < life
                  && battleScreen.getProjectiles().get(i).owner == this.owner) {
               alive = false;
               for (int j = 0; j < 4; j++) {
                  battleScreen.addParticle(new Particle(pos.x + 10, pos.y + 10,
                        (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
               }
               return;
            }
         }
         velocity.x = Vector
               .add(Vector.normalize(Vector.sub(new Vector(targetPlayer.center().x, 0), new Vector(center().x, 0)))
                     .mul(8), new Vector(velocity.x, 0).mul(19))
               .div(20).x;
         boolean touchingStage = move(velocity, true);
         if (velocity.x > 0)
            direction = 1;
         else
            direction = -1;
         hitbox.setPosition(pos);
         if (touchingStage) {
            if (life % 2 == 0) {
               if (++frame >= 4) {
                  frame = 0;
               }
            }
            velocity.y = 0.5;
            if (targetPlayer.pos.y + targetPlayer.selectedChar.height + 5 < pos.y + height && --jumpCooldown <= 0
                  && distance < 180) {
               jumpCooldown = 30;
               velocity.y = -30;
            }
         } else {
            velocity.y += 5;
         }
         incrementHitPlayer(-1);
         hittingPlayer();
      } else {
         frame++;
         if (frame > 12)
            frame = 12;
      }
      if (--life < 0)
         alive = false;
   }

   @Override
   public void onHitPlayer(Player player) {
      dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
            .rotationOf();
      hitPlayer(player, 10);
   }

   @Override
   public void kill() {
      for (int i = 0; i < 4; i++) {
         battleScreen.addParticle(new Particle(pos.x + width / 2,
               pos.y + height / 2, (Math.random() - 0.5) * 3,
               (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
      }
   }
}