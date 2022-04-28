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
      pos.x += width * 4 - width /2;
      pos.y += height * 4 - height / 2;
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
            alive = false;
         }
         frameTimer = 0;
      }

      pos.add(velocity);
   }

   @Override
   public void render(Graphics g, BattleScreen battleScreen, ImageObserver target) {
      battleScreen.renderObject(g, Images.getImage(fileName), pos, (int) (width * scale),
            (int) (height * scale), frame, false, target);
   }
}