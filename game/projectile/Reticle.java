package game.projectile;

import game.physics.*;
import game.Player;

public class Reticle extends HomingProjectile {

   Player target;
   
   public Reticle(Player ownerPlayer) {
      alive = true;
      hitbox = new AABB(0, -1000, 40, 40);
      pos = hitbox.getPosition();
      this.ownerPlayer = ownerPlayer;
      owner = ownerPlayer.keyLayout;
      velocity = new Vector(0, 0);
      width = 40;
      height = 40;
      frame = 0;
      life = 120;
      draw = true;
      fileName = "reticle.png";
   }
   
   @Override
   public boolean drawPriority() {
      return true;
   }
   
   @Override
   public void updateAfterTargeting() {
      if (life == 120) {
         pos.x = battleScreen.getStageBounds().getCenter().x;
         hitbox.x = pos.x;
         this.target = battleScreen.getPlayerList().get(Math.random() > 0.5 ? 0 : 1);
      }
      if (--life == 0) {
         alive = false;
      }
      hitbox.setPosition(pos);
      if (life == 15 || life == 8 || life == 2) {
         frame ++;
      }
      velocity = Vector.sub(target.center(), center()).div(4);
      if (life == 15) {
         Vector spawnPos = null;
         if (Math.random() > 0.5) {
            spawnPos = new Vector(Math.random() > 0.5 ? battleScreen.getStage().getUnsafeBlastZone().x : battleScreen.getStage().getUnsafeBlastZone().getX2(), Math.random() * battleScreen.getStage().getUnsafeBlastZone().height + battleScreen.getStage().getUnsafeBlastZone().y);
         } else {
            spawnPos = new Vector(Math.random() * battleScreen.getStage().getUnsafeBlastZone().width + battleScreen.getStage().getUnsafeBlastZone().x, Math.random() > 0.5 ? battleScreen.getStage().getUnsafeBlastZone().y : battleScreen.getStage().getUnsafeBlastZone().getY2());
         }
         battleScreen.addProjectileAtCenter(new HomingBolt(spawnPos.x, spawnPos.y, Vector.normalize(Vector.sub(target.center(), spawnPos)).mul(8).x, Vector.normalize(Vector.sub(target.center(), spawnPos)).mul(8).y, ownerPlayer, target));
      }
      
      if (life > 15) {
         move(velocity, false);
      } 
   }
   
   @Override
   public void kill() {
   }
}