package game.particle;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import game.Images;
import game.screen.BattleScreen;

public class AnimatedParticle extends Particle {
   int frameTime;
   int frame;
   int frameTimer;
   int frameCount;

   public AnimatedParticle(double x, double y, double velX, double velY, double scale, int width, int height,
         String fileName, int frameTime, int frameCount) {
      super(x, y, velX, velY, scale, width, height, fileName);
      this.frameTime = frameTime;
      frameTimer = 0;
      frame = 0;
      this.frameCount = frameCount;
   }

   @Override
   public void update() {
      if (++frameTimer >= frameTime && frameTime >= 0) {
         if (++frame >= frameCount) {
            frame = 0;
         }
         frameTimer = 0;
      }

      pos.add(velocity);
      scale -= 0.125;
      pos.x += width * scale / 16;
      pos.y += height * scale / 16;
      if (scale < 0.125)
         alive = false;
   }

   public void render(Graphics g, BattleScreen battleScreen, ImageObserver target) {
      battleScreen.renderObject(g, Images.getImage(fileName), pos, (int) (width * 4 * scale),
            (int) (height * 4 * scale), frame, false, target);
   }
}