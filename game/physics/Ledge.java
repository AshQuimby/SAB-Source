package game.physics;

import game.GameObject;

public class Ledge extends GameObject {
   public AABB hitbox;
   public int direction;

   public Ledge(int x, int y, int width, int height, int direction) {
      hitbox = new AABB(x, y, width, height);
      this.direction = direction;
   }

   public int getDirection() {
      return direction;
   }

   public Vector getPos() {
      return hitbox.getPosition();
   }

   public Ledge grabbing(AABB hitbox) {
      AABB col1 = this.hitbox;
      AABB col2 = hitbox;
      if (col1.overlaps(col2)) {
         return this;
      }
      return null;
   }
}