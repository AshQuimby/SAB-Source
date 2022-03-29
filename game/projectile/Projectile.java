package game.projectile;

import java.util.List;
import java.util.ArrayList;

import game.GameObject;
import game.Player;
import game.AssBall;
import game.physics.*;
import game.screen.BattleScreen;
import game.Images;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public abstract class Projectile extends GameObject {
   public int direction = 1;
   public int frame = 0;
   public boolean alive;
   public AABB hitbox;
   public int damage;
   public Vector velocity = new Vector(0, 0);
   public double knockbackStrength;
   public double dir;
   public int width;
   public int height;
   public int life;
   public int owner;
   public int hitPlayer = 0;
   public int updatesPerTick = 1;
   public Player ownerPlayer;
   public boolean unreflectable = false;
   public boolean draw = true;
   public boolean unParryable = false;
   public String fileName;
   public BattleScreen battleScreen;
   public Vector hitboxOffset = new Vector(0, 0);

   public abstract void update();

   public abstract void kill();

   public boolean drawPriority() {
      return false;
   }

   public List<Player> hittingPlayer() {
      List<Player> hittingPlayers = new ArrayList<>();
      for (Player player : battleScreen.getPlayerList()) {
         if (hitbox.overlaps(player.hitbox) && player != ownerPlayer && !player.invincible && alive) {
            if (player.parryTimer > 0) {
               alive = false;
            }
            if (hitPlayer < 1) {
               if (!overrideHitPlayer()) {
                  onHitPlayer(player);
                  hitPlayer(damage, knockbackStrength, dir, player);
                  player.velocity.x = 0;
                  player.velocity.y = 0;
               }
               hittingPlayers.add(player);
            }
         }
      }
      for (AssBall assBall : battleScreen.getAssBalls()) {
         AABB assHitbox = new AABB(assBall.pos.x, assBall.pos.y, 40, 40);
         if (hitbox.overlaps(assHitbox)) {
            if (hitPlayer < 1) {
               if (!overrideHitPlayer()) {
                  onHitPlayer(assBall.fakePlayer);
                  assBall.hit(damage, knockbackStrength, dir, ownerPlayer);
               }
            }
         }
      }
      if (hittingPlayers.size() > 0) {
         return hittingPlayers;
      }
      return null;
   }

   public boolean staticKnockback() {
      return false;
   }

   public void hitPlayer(int damage, double kb, double dir, Player player) {
      player.hitPlayer(damage, kb, dir, staticKnockback() ? 0 : 0.01, this);
   }

   public void onHitPlayer(Player player) {
   }

   protected boolean overrideHitPlayer() {
      return false;
   }

   public void render(Graphics g, ImageObserver target) {
      BufferedImage image = Images.getImage(fileName);

      if (direction < 0 && draw) {
         battleScreen.renderObject(g, image, pos, width, height, frame, true, target);
      } else if (draw) {
         battleScreen.renderObject(g, image, pos, width, height, frame, false, target);
      }
   }

   public Vector center() {
      return new Vector(pos.x + width / 2, pos.y + height / 2);
   }

   public boolean move(Vector step, boolean collideWithStage) {
      boolean collided = false;

      pos.x += step.x;
      hitbox.x = pos.x + hitboxOffset.x;

      if (collideWithStage) {
         List<AABB> collisions = battleScreen.getStage().getCollisions(hitbox);
         if (collisions.size() > 0)
            collided = true;

         hitbox.resolveX(step.x, collisions);
      }

      pos.y += step.y;
      hitbox.y = pos.y + hitboxOffset.y;

      if (collideWithStage) {
         List<AABB> collisions = battleScreen.getStage().getCollisions(hitbox);
         if (collisions.size() > 0)
            collided = true;
         hitbox.resolveY(step.y, collisions);
      }

      pos.x = hitbox.x - hitboxOffset.x;
      pos.y = hitbox.y - hitboxOffset.y;

      return collided;
   }
}