package game.projectile;

public abstract class BufferedProjectile extends Projectile {
   int bufferTicks;

   @Override
   public void update() {
      if (--bufferTicks <= 0) {
         if (bufferTicks == 0)
            onUnbuffer();
         updatePostBuffer();
      } else {
         updateWhileBuffering();
      }
   }

   public abstract void updateWhileBuffering();

   public abstract void onUnbuffer();

   public abstract void updatePostBuffer();
}