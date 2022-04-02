package game.physics;

import java.io.Serializable;

public class Vector implements Serializable {
   public double x;
   public double y;

   public Vector(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public static Vector add(Vector vector1, Vector vector2) {
      return new Vector(vector1.x + vector2.x, vector1.y + vector2.y);
   }

   public void add(Vector vector) {
      this.x += vector.x;
      this.y += vector.y;
   }

   public static double distanceBetween(Vector vector, Vector other) {
      return Math.sqrt((vector.x - other.x) * (vector.x - other.x)
            + (vector.y - other.y) * (vector.y - other.y));
   }

   public Vector add(double x, double y) {
      return new Vector(this.x + x, this.y + y);
   }

   public static Vector sub(Vector vector1, Vector vector2) {
      return new Vector(vector1.x - vector2.x, vector1.y - vector2.y);
   }

   public Vector mul(double factor) {
      return new Vector(x * factor, y * factor);
   }

   public Vector div(double denominator) {
      return new Vector(x / denominator, y / denominator);
   }

   public static Vector normalize(Vector vector) {
      return vector.div(vector.len());
   }

   public static Vector clone(Vector vector) {
      return new Vector(vector.x, vector.y);
   }

   public Vector clone() {
      return new Vector(this.x, this.y);
   }

   public Vector rotateBy(double radians) {
      Vector newVector = new Vector(Math.cos(radians) * x - Math.sin(radians) * y,
            Math.sin(radians) * x + Math.cos(radians) * y);
      this.x = newVector.x;
      this.y = newVector.y;
      return newVector;
   }

   public double len() {
      return Math.sqrt(x * x + y * y);
   }

   public double rotationOf() {
      return Math.atan2(this.y, this.x);
   }

   public Vector snap(int gridSize) {
      return new Vector(Math.floor(x / gridSize) * gridSize, Math.floor(y / gridSize) * gridSize);
   }

   public static Vector zero() {
      return new Vector(0, 0);
   }
}