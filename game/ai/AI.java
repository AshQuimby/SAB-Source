package game.ai;

import java.io.Serializable;

import game.AssBall;
import game.Player;
import java.util.List;

import game.physics.Ledge;
import game.physics.Vector;
import game.stage.Platform;
import game.projectile.Projectile;

public class AI implements Serializable {
   Player owner;

   public void preUpdate(Player player) {
      owner = player;
      update(player);
   }

   public void update(Player player) {

   }

   public Player targetPlayer(List<Player> players) {
      double bestDist = 25600;
      Player bestTarget = null;
      for (Player player : players) {
         double dist = Vector.distanceBetween(player.hitbox.getCenter(), owner.hitbox.getCenter());
         if (dist < bestDist && player != owner) {
            bestTarget = player;
            bestDist = dist;
         }
      }
      return bestTarget;
   }

   public Projectile nearestEnemyProjectile(List<Projectile> projectiles) {
      double bestDist = 25600;
      Projectile bestTarget = null;
      for (Projectile projectile : projectiles) {
         double dist = Vector.distanceBetween(projectile.hitbox.getCenter(), owner.hitbox.getCenter());
         if (dist < bestDist && projectile.ownerPlayer != owner) {
            bestTarget = projectile;
            bestDist = dist;
         }
      }
      return bestTarget;
   }

   public Platform nearestPlatform(List<Platform> platforms) {
      double bestDist = 25600;
      Platform bestTarget = null;
      for (Platform platform : platforms) {
         double dist = Vector.distanceBetween(platform.getHitbox().getCenter(), owner.hitbox.getCenter());
         if (dist < bestDist) {
            bestTarget = platform;
            bestDist = dist;
         }
      }
      return bestTarget;
   }

   public AssBall nearestAssBall(List<AssBall> assBalls) {
      double bestDist = 25600;
      AssBall bestTarget = null;
      for (AssBall assBall : assBalls) {
         double dist = Vector.distanceBetween(assBall.pos, owner.hitbox.getCenter());
         if (dist < bestDist) {
            bestTarget = assBall;
            bestDist = dist;
         }
      }
      return bestTarget;
   }

   public static Platform nearestPlatform(Player player, List<Platform> platforms) {
      double bestDist = 25600;
      Platform bestTarget = null;
      for (Platform platform : platforms) {
         double dist = Vector.distanceBetween(platform.getHitbox().getCenter(), player.hitbox.getCenter());
         if (dist < bestDist) {
            bestTarget = platform;
            bestDist = dist;
         }
      }
      return bestTarget;
   }

   public Ledge nearestLedge(List<Ledge> ledges) {
      double bestDist = 25600;
      Ledge bestTarget = null;
      for (Ledge ledge : ledges) {
         double dist = Vector.distanceBetween(owner.center(), ledge.hitbox.getCenter());
         if (dist < bestDist) {
            bestTarget = ledge;
            bestDist = dist;
         }
      }
      return bestTarget;
   }

   public Platform nearestPlatformBelow(List<Platform> platforms) {
      double bestDist = 25600;
      Platform bestTarget = null;
      for (Platform platform : platforms) {
         double dist = Vector.distanceBetween(platform.getHitbox().getCenter(), owner.hitbox.getCenter());
         if (dist < bestDist && platform.getHitbox().y > owner.hitbox.getY2()) {
            bestTarget = platform;
            bestDist = dist;
         }
      }
      return bestTarget;
   }
}