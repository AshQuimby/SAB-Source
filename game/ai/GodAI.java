package game.ai;

import game.Player;
import game.stage.Platform;
import game.physics.Vector;
import game.physics.CollisionLine;
import game.projectile.Projectile;
import game.physics.AABB;

public class GodAI extends AI {

   boolean[] toBePressed = { false, false, false, false, false, false, false };

   public void press(int readableKeyID) {
      toBePressed[readableKeyID] = true;
   }

   public void tap(int readableKeyID) {
      if (!owner.readableKeys[readableKeyID])
         toBePressed[readableKeyID] = true;
   }

   public void release(int readableKeyID) {
      toBePressed[readableKeyID] = false;
   }

   @Override
   public void update(Player player) {
      Player target = targetPlayer(player.battleScreen.getPlayerList());
      Platform targetPlatform = nearestPlatform(player.battleScreen.getStage().getPlatforms());
      Projectile targetProjectile = nearestEnemyProjectile(player.battleScreen.getProjectiles());
      double distance = Vector.distanceBetween(target.center(), player.center());
      Vector nearestPlatformPoint = targetPlatform.getHitbox().nearestPointTo(player.hitbox.getCenter());
      Vector platformDistances = new Vector(Math.abs(nearestPlatformPoint.x - player.center().x), Math.abs(nearestPlatformPoint.y - player.center().y));
      double projectileDistance = 25600;
      
      Platform nearestPlatformBelow = nearestPlatformBelow(player.battleScreen.getStage().getPlatforms());
      
      Vector targetPointOnPlatform = targetPlatform.getHitbox().nearestPointTo(target.center());
      
      Vector targetPos = target.hitbox.getCenter().clone();
      AABB nextTargetProjectileFrame = null;

      if (targetProjectile != null) {
         projectileDistance = Vector.distanceBetween(targetProjectile.hitbox.getCenter(), player.center());
         nextTargetProjectileFrame = targetProjectile.hitbox.copy();
         if (targetProjectile.velocity != null) {
            nextTargetProjectileFrame.setPosition(Vector.add(targetProjectile.pos, targetProjectile.velocity));
         }
      }

      if (nearestAssBall(player.battleScreen.getAssBalls()) != null) {
         targetPos = nearestAssBall(player.battleScreen.getAssBalls()).pos;
      }

      if (target != null && targetPos != null) {
         release(Player.DOWN);
         release(Player.ATTACK);
         release(Player.RIGHT);
         release(Player.LEFT);
         release(Player.JUMP);
         release(Player.UP);
         release(Player.PARRY);
         
         CollisionLine platformScan = new CollisionLine(player.center().x, player.center().y, 2560, Math.toRadians(90));
            if (!platformScan.collidingWithHitbox(targetPlatform.getHitbox()) && platformDistances.x > 256) {
               targetPos = Vector.sub(targetPointOnPlatform, new Vector(0, 128));
            }
            if (player.falling && platformDistances.x > 256) {
               targetPos = Vector.sub(targetPlatform.getHitbox().getCenter(), new Vector(0, -128));
            }
            
            if (!platformScan.collidingWithHitbox(targetPlatform.getHitbox()) && platformDistances.x > 256) {
               targetPos = Vector.sub(targetPointOnPlatform, new Vector(0, 128));
            }
            
            if (player.falling && nearestPlatformBelow != null) {
               targetPos = new Vector(nearestPlatformBelow.getHitbox().getRandomPoint().x, nearestPlatformBelow.getHitbox().y);
            }
            
            if (target.hitbox.y > player.battleScreen.getStage().getSafeBlastZone().getY2() - 128) {
               targetPos = Vector.sub(targetPointOnPlatform, new Vector(0, 128));
            }
         
         if (targetPos.x > player.center().x && distance > 30) {
            press(Player.RIGHT);
         }
         
         if (targetPos.x < player.center().x && distance > 30) {
            press(Player.LEFT);
         }
         
         if (targetPos.y > player.hitbox.y + 256 && distance < 320 && (target.endLag > 0 || !player.falling) || distance < 30) {
            release(Player.ATTACK);
            if (targetPos.x > player.center().x) {
               press(Player.LEFT);
               release(Player.RIGHT);
            }
            
            if (targetPos.x < player.center().x) {
               press(Player.RIGHT);
               release(Player.LEFT);
            }
         }
         
         if ((targetPos.y < player.center().y - 32)
               && player.velocity.y > 0) {
            if (player.jumps > 0) {
               tap(Player.JUMP);
            } else if (!player.falling) {
               tap(Player.UP);
               tap(Player.ATTACK);
            }
         }
         if (distance < 32) {
            if (Math.random() > 0.6 || player.finalAss) {
               release(Player.LEFT);
               release(Player.RIGHT);
            }
            tap(Player.ATTACK);
         } else if (distance < 64 && player.finalAss) {
            release(Player.LEFT);
            release(Player.RIGHT);
            tap(Player.ATTACK);
         } else if (distance < 320) {
            if (Math.random() > 0.9) {
               tap(Player.ATTACK);
            }
         }
         if (targetProjectile != null && nextTargetProjectileFrame.overlaps(player.hitbox) && Math.random() > 0.8) {
            tap(Player.PARRY);
         }
         if (targetProjectile != null && projectileDistance < 96) {
            if (player.jumps > 0) {
               tap(Player.JUMP);
            } else if (!player.falling && projectileDistance < 48) {
               tap(Player.UP);
               tap(Player.ATTACK);
            }
         }
         if ((targetPlatform.getHitbox().overlaps(player.hitbox) || player.touchingStage)
               && targetPlatform.canJumpThrough()
               && targetPos.y > player.center().y + 32) {
            press(Player.DOWN);
         }
         if (distance < 128 && target.damage > 100 && Math.random() > 0.9 || player.charge > 0) {
            press(Player.DOWN);
            press(Player.ATTACK);
         }
         if (player.charge > 30 || player.charge > 0 && (targetPos.x - player.center().x)
               / (Math.abs(targetPos.x - targetPos.x)) != player.direction) {
            release(Player.ATTACK);
         }
      }
      if (player.frozen > 1) {
         for (int i = 0; i < 6; i++) {
            if (Math.random() > 0.9)
               tap(i);
         }
      }
      if (player.grabbingLedge) {
         if (player.ledgeGrabs > 1) {
            tap(Player.ATTACK);
         } else {
            tap(Player.JUMP);
         }
      }
      for (int i = 0; i < player.readableKeys.length; i++) {
         if (toBePressed[i])
            player.simulateKeyPress(i);
         else if (!toBePressed[i] && player.readableKeys[i])
            player.simulateKeyRelease(i);
      }
   }
}

// package game.ai;
// 
// import java.util.List;
// 
// import game.Player;
// import game.stage.Platform;
// import game.physics.Vector;
// import game.physics.CollisionLine;
// import game.physics.Ledge;
// import game.projectile.Projectile;
// import game.physics.AABB;
// 
// public class GodAI extends AI {
// 
//    boolean[] toBePressed = { false, false, false, false, false, false, false };
//    private Navigator navigator;
// 
//    public void press(int readableKeyID) {
//       toBePressed[readableKeyID] = true;
//    }
// 
//    public void tap(int readableKeyID) {
//       if (!owner.readableKeys[readableKeyID])
//          toBePressed[readableKeyID] = true;
//    }
// 
//    public void release(int readableKeyID) {
//       toBePressed[readableKeyID] = false;
//    }
// 
//    @Override
//    public void update(Player player) {
//       Player target = targetPlayer(player.battleScreen.getPlayerList());
//       Platform targetPlatform = nearestPlatform(player.battleScreen.getStage().getPlatforms());
//       Projectile targetProjectile = nearestEnemyProjectile(player.battleScreen.getProjectiles());
//       double distance = Vector.distanceBetween(target.center(), player.center());
//       Vector nearestPlatformPoint = targetPlatform.getHitbox().nearestPointTo(player.hitbox.getCenter());
//       Vector platformDistances = new Vector(Math.abs(nearestPlatformPoint.x - player.center().x),
//             Math.abs(nearestPlatformPoint.y - player.center().y));
//       double projectileDistance = 25600;
// 
//       Platform nearestPlatformBelow = nearestPlatformBelow(player.battleScreen.getStage().getPlatforms());
// 
//       Platform nearestPlatformToTarget = AI.nearestPlatform(target, player.battleScreen.getStage().getPlatforms());
// 
//       Vector targetPointOnPlatform = targetPlatform.getHitbox().nearestPointTo(target.center());
// 
//       Vector targetNearestPlatformPoint = nearestPlatformToTarget.getHitbox().nearestPointTo(target.center());
// 
//       Vector targetPos = target.hitbox.getCenter().clone();
//       AABB nextTargetProjectileFrame = null;
// 
//       navigator = new Navigator(player.battleScreen.getStage());
//       List<Vector> path = navigator.getPath(player.center(), targetPos);
// 
//       if (path != null) {
//          targetPos = path.get(1);
//       }
// 
//       if (targetProjectile != null) {
//          projectileDistance = Vector.distanceBetween(targetProjectile.hitbox.getCenter(), player.center());
//          nextTargetProjectileFrame = targetProjectile.hitbox.copy();
//          if (targetProjectile.velocity != null) {
//             nextTargetProjectileFrame.setPosition(Vector.add(targetProjectile.pos, targetProjectile.velocity));
//          }
//       }
//       if (target != null && targetPos != null) {
//          release(Player.DOWN);
//          release(Player.ATTACK);
//          release(Player.RIGHT);
//          release(Player.LEFT);
//          release(Player.JUMP);
//          release(Player.UP);
//          release(Player.PARRY);
// 
//          boolean platformScan = new CollisionLine(player.center().x, player.center().y, 2560,
//                Math.toRadians(90)).collidingWithHitboxes(player.battleScreen.getStage().getHitboxes());
// 
//          boolean targetPlatformScan = new CollisionLine(target.center().x, target.center().y, 2560,
//                Math.toRadians(90)).collidingWithHitboxes(player.battleScreen.getStage().getHitboxes());
// 
//          Ledge targetLedge = nearestLedge(player.battleScreen.getStage().ledges);
// 
//          if (!targetPlatformScan && (player.jumps < 1 || player.falling)) {
//             targetPos = new Vector(targetNearestPlatformPoint.x, nearestPlatformToTarget.getHitbox().y - 16);
//          }
// 
//          if (targetLedge != null && !platformScan) {
//             targetPos = targetLedge.hitbox.getCenter();
//          }
// 
//          if (targetPos.x > player.center().x && distance > 30) {
//             press(Player.RIGHT);
//          }
// 
//          if (targetPos.x < player.center().x && distance > 30) {
//             press(Player.LEFT);
//          }
// 
//          if (targetPos.y > player.hitbox.y + 256 && distance < 320 && (target.endLag > 0 || !player.falling)
//                || distance < 30) {
//             release(Player.ATTACK);
//             if (targetPos.x > player.center().x) {
//                press(Player.LEFT);
//                release(Player.RIGHT);
//             }
// 
//             if (targetPos.x < player.center().x) {
//                press(Player.RIGHT);
//                release(Player.LEFT);
//             }
//          }
// 
//          if ((targetPos.y < player.center().y - 32)
//                && player.velocity.y > 0) {
//             if (player.jumps > 0) {
//                tap(Player.JUMP);
//             } else if (!player.falling) {
//                tap(Player.UP);
//                tap(Player.ATTACK);
//             }
//          }
//          if (distance < 32) {
//             if (Math.random() > 0.6) {
//                release(Player.LEFT);
//                release(Player.RIGHT);
//             }
//             tap(Player.ATTACK);
//          } else if (distance < 320) {
//             if (Math.random() > 0.9) {
//                tap(Player.ATTACK);
//             }
//          }
//          if (targetProjectile != null && nextTargetProjectileFrame.overlaps(player.hitbox) && Math.random() > 0.5) {
//             tap(Player.PARRY);
//          }
//          if (targetProjectile != null && projectileDistance < 96) {
//             if (player.jumps > 0) {
//                tap(Player.JUMP);
//             } else if (!player.falling && projectileDistance < 48) {
//                tap(Player.UP);
//                tap(Player.ATTACK);
//             }
//          }
//          if ((targetPlatform.getHitbox().overlaps(player.hitbox) || player.touchingStage)
//                && targetPlatform.canJumpThrough()
//                && targetPos.y > player.center().y + 32) {
//             press(Player.DOWN);
//          }
//          if (distance < 128 && target.damage > 100 && Math.random() > 0.9 || player.charge > 0) {
//             press(Player.DOWN);
//             press(Player.ATTACK);
//          }
//          if (player.charge > 30 || player.charge > 0 && (targetPos.x - player.center().x)
//                / (Math.abs(targetPos.x - targetPos.x)) != player.direction) {
//             release(Player.ATTACK);
//          }
//       }
//       if (player.frozen > 1) {
//          for (int i = 0; i < 6; i++) {
//             if (Math.random() > 0.9)
//                tap(i);
//          }
//       }
//       if (player.grabbingLedge) {
//          if (player.ledgeGrabs > 1) {
//             tap(Player.ATTACK);
//          } else {
//             tap(Player.JUMP);
//          }
//       }
//       for (int i = 0; i < player.readableKeys.length; i++) {
//          if (toBePressed[i])
//             player.simulateKeyPress(i);
//          else if (!toBePressed[i] && player.readableKeys[i])
//             player.simulateKeyRelease(i);
//       }
//    }
// }