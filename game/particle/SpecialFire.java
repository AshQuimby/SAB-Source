package game.particle;

public class SpecialFire extends Particle {
   public float scaleSpeed;

   public int timeLeft;

   public SpecialFire(double x, double y, double velX, double velY, double scale, int width, int height,
         String fileName,
         float scaleSpeed) {
      super(x, y, velX, velY, scale, width, height, fileName);
      this.scaleSpeed = scaleSpeed;
      this.scale = scale;
      timeLeft = 60;
   }

   @Override
   public void update() {
      pos.add(velocity);
      scale += scaleSpeed;
      pos.x += width * scale / 16;
      pos.y += height * scale / 16;
      scaleSpeed -= 0.0125f;
      if (scaleSpeed < 0) {
         fileName = "smoke.png";
      }
      if (--timeLeft <= 0) {
         alive = false;
      }
      if (scale < 0.125)
         alive = false;
   }
}