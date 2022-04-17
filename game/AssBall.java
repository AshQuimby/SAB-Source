package game;

import game.screen.BattleScreen;
import game.particle.Particle;
import game.physics.*;

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

   public AssBall(BattleScreen battleScreen) {
      damage = 0;
      frameTimer = 0;
      frame = 0;
      alive = true;
      velocity = new Vector(0, 0);
      fakePlayer = new PsuedoPlayer(battleScreen);
      tempVelocity = new Vector(0, 0);
      this.battleScreen = battleScreen;
      pos = battleScreen.getStage().getUnsafeBlastZone().getRandomPoint();
   }

   public void update() {
      fakePlayer.hitbox.setCenter(center());
      fakePlayer.pos = pos;
      tempVelocity = tempVelocity.mul(0.9);
      pos.add(velocity);
      pos.add(tempVelocity);
      velocity = velocity.mul(0.98);
      velocity.x += Math.random() - 0.5;
      velocity.y += Math.random() - 0.5;
      battleScreen.changeAssBallTimer(1);
      velocity.add(new Vector(0.1, 0)
            .rotateBy(Vector.sub(battleScreen.getStage().getSafeBlastZone().getCenter(), pos).rotationOf()));
      if (++frameTimer >= 8) {
         if (++frame >= 3) {
            frame = 0;
         }
         frameTimer = 0;
      }
   }

   public Vector center() {
      return new Vector(pos.x + 20, pos.y + 20);
   }

   public void render(Graphics g, ImageObserver target) {
      BufferedImage image = Images.getImage("ball_sack.png");
      battleScreen.renderObject(g, image, new Vector(pos.x - 40, pos.y - 40), 120, 120, 0, true, target);

      image = Images.getImage("ass_ball.png");
      battleScreen.renderObject(g, image, pos, 40, 40, frame, true, target);
   }

   public void hit(int damage, double knockback, double knocbackDirection, Player player) {
      this.damage += damage;
      SoundEngine.playSound("ass_ball_hit");
      if (this.damage > 50) {
         SoundEngine.playSound("ass_ball_shatter");
         alive = false;
         for (int i = 0; i < 16; i++) {
            battleScreen.addParticle(new Particle(pos.x + 36, pos.y + 36, (Math.random() - 0.5) * 16,
                  (Math.random() - 0.5) * 16, 2, 6, 6, "twinkle.png"));
         }
         player.finalAss = true;
      }
      tempVelocity.add(new Vector(knockback, 0).rotateBy(knocbackDirection));
   }
}