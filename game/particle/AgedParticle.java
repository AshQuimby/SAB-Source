package game.particle;

public class AgedParticle extends Particle {

   int life;

   public AgedParticle(double x, double y, double velX, double velY, double scale, int width, int height,
         String fileName, int life) {
      super(x, y, velX, velY, scale, width, height, fileName);
      this.life = life;
   }

   public void update() {
      pos.add(velocity);
      if (--life <= 0) {
         alive = false;
      }
   }
}