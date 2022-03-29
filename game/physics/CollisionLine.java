package game.physics;

import game.GameObject;
import java.util.List;

public class CollisionLine extends GameObject {
   private AABB hitbox;
   private double length;
   private double dir;

   public CollisionLine(double x, double y, double length, double rotation) {
      hitbox = new AABB(x, y, 1, 1);
      this.length = length;
      dir = rotation;
   }

   public boolean collidingWithHitbox(AABB hitbox) {
      boolean colliding = false;
      Vector pos = hitbox.getPosition();
      for (int i = 0; i < length; i++) {
         if (!colliding)
            colliding = new AABB(pos.x, pos.y, hitbox.width, hitbox.height).overlaps(this.hitbox);
         pos.add(new Vector(-1, 0).rotateBy(dir));
      }
      return colliding;
   }

   public boolean collidingWithHitboxes(List<AABB> hitboxes) {
      for (AABB hitbox : hitboxes) {
         if (collidingWithHitbox(hitbox)) {
            return true;
         }
      }

      return false;
   }
}