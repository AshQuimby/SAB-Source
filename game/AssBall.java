package game;

import game.screen.BattleScreen;
import game.particle.Particle;
import game.physics.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class AssBall extends GameObject {

   private int damage;
   private int frameTimer;
   private int frame;
   private Vector velocity;
   private Vector tempVelocity;
   private BattleScreen battleScreen;
   public PsuedoPlayer fakePlayer;
   public boolean alive;
   private int rgbTimer;
   private int rgbOffset;

   public AssBall(BattleScreen battleScreen) {
      damage = 0;
      frameTimer = 0;
      frame = 0;
      rgbTimer = 0;
      alive = true;
      velocity = new Vector(0, 0);
      fakePlayer = new PsuedoPlayer(battleScreen, true);
      fakePlayer.hitbox.width = 40;
      fakePlayer.hitbox.height = 40;
      tempVelocity = new Vector(0, 0);
      this.battleScreen = battleScreen;
      pos = battleScreen.getStage().getUnsafeBlastZone().getRandomPoint();
   }

   public void update() {
      if (!Settings.performanceMode()) {
         rgbTimer += 8;
         if (rgbTimer > 511) {
            rgbTimer = 0;
            if (++rgbOffset >= 3) {
               rgbOffset = 0;
            }
         }
      }
      fakePlayer.invincible = false;
      if (fakePlayer.knockBack.len() > 0)
         hit(fakePlayer.lastHitBy);
      fakePlayer.hitbox.setCenter(center());
      fakePlayer.parryTimer = -10;
      fakePlayer.pos = pos;
      tempVelocity = tempVelocity.mul(0.9);
      pos.add(velocity);
      pos.add(tempVelocity);
      velocity = velocity.mul(0.99);
      velocity.x += Math.random() - 0.5;
      velocity.y += Math.random() - 0.5;
      battleScreen.changeAssBallTimer(1);
      velocity.add(new Vector(0.1, 0)
            .rotateBy(Vector.sub(battleScreen.getStage().getSafeBlastZone().getCenter(), pos).rotationOf()));
      if (++frameTimer >= 2) {
         if (++frame >= 18 || frame >= 15) {
            frame = 0;
         }
         frameTimer = 0;
      }
   }

   public Vector center() {
      return new Vector(pos.x + 20, pos.y + 20);
   }

   public void render(Graphics g, ImageObserver target) {
      if (!Settings.performanceMode()) {
         BufferedImage image = Images.getImage("ball_sack.png");
         int colorVal1 = Math.min(255, 511 - rgbTimer);
         int colorVal2 = Math.min(255, rgbTimer);
         image = Images.colorAverageEffect(image,
               new Color((rgbOffset == 0 ? colorVal1 : rgbOffset == 2 ? colorVal2 : 0),
                     (rgbOffset == 1 ? colorVal1 : rgbOffset == 0 ? colorVal2 : 0),
                     (rgbOffset == 2 ? colorVal1 : rgbOffset == 1 ? colorVal2 : 0)));
         battleScreen.renderObject(g, image, new Vector(pos.x - 40, pos.y - 40), 120, 120, 0, true, target);
   
         image = Images.getImage("ass_ball.png");
         image = Images.colorEffect(image,
               new Color((rgbOffset == 0 ? colorVal1 : rgbOffset == 2 ? colorVal2 : 0),
                     (rgbOffset == 1 ? colorVal1 : rgbOffset == 0 ? colorVal2 : 0),
                     (rgbOffset == 2 ? colorVal1 : rgbOffset == 1 ? colorVal2 : 0)));
         battleScreen.renderObject(g, image, pos, 40, 40, true, target);
   
         image = Images.getImage("ass_ball_overlay.png");
         battleScreen.renderObject(g, image, pos, 40, 40, frame, true, target);
      } else {
         BufferedImage image = Images.getImage("ball_sack.png");
         battleScreen.renderObject(g, image, new Vector(pos.x - 40, pos.y - 40), 120, 120, true, target);
         image = Images.getImage("ass_ball_color.png");
         battleScreen.renderObject(g, image, pos, 40, 40, frame, true, target);
      }
   }

   public void hit(Player player) {
      damage += fakePlayer.damage;
      SoundEngine.playSound("ass_ball_hit");
      if (damage > 50) {
         SoundEngine.playSound("ass_ball_shatter");
         fakePlayer.removeFromBattlescreen();
         alive = false;
         fakePlayer.lives = 0;
         for (int i = 0; i < 16; i++) {
            battleScreen.addParticle(new Particle(pos.x + 36, pos.y + 36, (Math.random() - 0.5) * 16,
                  (Math.random() - 0.5) * 16, 2, 6, 6, "twinkle.png"));
         }
         if (player != null)
            player.finalAss = true;
      }
      fakePlayer.damage = 0;
      tempVelocity.add(fakePlayer.knockBack.div(4));
      fakePlayer.knockBack = new Vector(0, 0);
   }
}