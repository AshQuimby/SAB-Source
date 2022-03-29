package game.particle;

public class GravityParticle extends Particle {
   double mass;

   public GravityParticle(double x, double y, double velX, double velY, double scale, int width, int height,
         String fileName, double mass) {
      super(x, y, velX, velY, scale, width, height, fileName);
      this.mass = mass;
   }

   public void update() {
      velocity.y += mass;
      pos.add(velocity);
      if (pos.y > 900)
         alive = false;
   }
}