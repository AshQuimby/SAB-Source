package game.particle;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import game.GameObject;
import game.physics.Vector;
import game.Images;
import game.Settings;
import game.screen.BattleScreen;

public class Particle extends GameObject {
   public Vector velocity;
   public double scale;
   public boolean alive;
   public int width;
   public int height;
   public String fileName;

   public Particle(double x, double y, double velX, double velY, double scale, int width, int height, String fileName) {
      pos = new Vector(x - width * 4, y - height * 4);
      velocity = new Vector(velX, velY);
      this.scale = scale;
      this.width = width;
      this.height = height;
      alive = true;
      this.fileName = fileName;
   }

   public void update() {
      pos.add(velocity);
      scale -= 0.125;
      pos.x += width * scale / 16;
      pos.y += height * scale / 16;
      if (scale < 0.125 || (scale < 0.25 && Settings.performanceMode()))
         alive = false;
   }

   public void render(Graphics g, BattleScreen battleScreen, ImageObserver target) {
      battleScreen.renderObject(g, Images.getImage(fileName), pos, (int) (width * 4 * scale),
            (int) (height * 4 * scale), false, target);
   }
}