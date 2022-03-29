package game.physics;

import java.util.List;
import java.io.Serializable;

public class AABB implements Serializable {
   public double x;
   public double y;
   public double width;
   public double height;

   public AABB(double x, double y, double width, double height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public double getX2() {
      return x + width;
   }

   public double getY2() {
      return y + height;
   }

   public double getCenterX() {
      return this.x + this.width / 2;
   }

   public double getCenterY() {
      return this.y + this.height / 2;
   }

   public Vector getCenter() {
      return new Vector(x + width / 2, y + height / 2);
   }

   public void setPosition(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public void setPosition(Vector position) {
      x = position.x;
      y = position.y;
   }

   public Vector getPosition() {
      return new Vector(x, y);
   }

   public void setCenterX(double x) {
      this.x = x - this.width / 2;
   }

   public void setCenterY(double y) {
      this.y = y - this.height / 2;
   }

   public void setCenter(double x, double y) {
      this.x = x - this.width / 2;
      this.y = y - this.height / 2;
   }

   public void setCenter(Vector center) {
      x = center.x - width / 2;
      y = center.y - height / 2;
   }

   public void setX2(double x2) {
      this.x = x2 - this.width;
   }

   public void setY2(double y2) {
      this.y = y2 - this.height;
   }

   private boolean rangeOverlaps(double a, double b, double c, double d) {
      return a < d && b > c;
   }

   public boolean overlaps(AABB other) {
      return rangeOverlaps(x, x + width, other.x, other.x + other.width) &&
            rangeOverlaps(y, y + height, other.y, other.y + other.height);
   }

   public void resolveX(double dx, AABB other) {
      if (overlaps(other)) {
         if (dx > 0) {
            setX2(other.x);
         } else {
            x = other.getX2();
         }
      }
   }

   public void resolveY(double dy, AABB other) {
      if (overlaps(other)) {
         if (dy > 0) {
            setY2(other.y);
         } else {
            y = other.getY2();
         }
      }
   }

   public void resolveX(double dx, List<AABB> collisions) {
      for (AABB other : collisions) {
         resolveX(dx, other);
      }
   }

   public void resolveY(double dy, List<AABB> collisions) {
      for (AABB other : collisions) {
         resolveY(dy, other);
      }
   }

   public Vector getRandomPoint() {
      return new Vector(x + Math.random() * width, y + Math.random() * height);
   }

   public static Vector randomPointOn(AABB hitbox) {
      return new Vector(hitbox.x + Math.random() * hitbox.width, hitbox.y + Math.random() * hitbox.height);
   }
   
   public AABB copy() {
      return new AABB(x, y, width, height);
   }
   
   // Changes width and height around the same center point
   public void transformDimensions(int width, int height) {
      double oldWidth = this.width;
      double oldHeight = this.height;
      
      setWidth(width, false);
      setHeight(height, false);
      
      x += oldWidth / 2 - this.width / 2;
      y += oldHeight / 2 - this.height / 2;
   }
   
   // Changes width and height
   public void setDimensions(int width, int height, boolean anchorRight, boolean anchorBottom) {
      setWidth(width, anchorRight);
      setHeight(height, anchorBottom);
   }
   
   public void setWidth(int width, boolean anchorRight) {
      double oldWidth = this.width;
      
      this.width = width;
      if (anchorRight)
         x -= width + oldWidth;
   }
   
   public void setHeight(int height, boolean anchorBottom) {
      double oldHeight = this.height;
      
      this.height = height;
      if (anchorBottom)
         x -= height + oldHeight;
   }
   
   public Vector nearestPointTo(Vector point) {
      if (point.x < x)
         point.x = x;
      if (point.x > getX2())
         point.x = getX2();
      if (point.y < y)
         point.y = y;
      if (point.y > getY2())
         point.y = getY2();
      return point;
   }
}