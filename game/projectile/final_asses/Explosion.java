package game.projectile.final_asses;

import game.Player;
import game.physics.*;
import game.projectile.Projectile;
import game.particle.Particle;
import game.SoundEngine;

public class Explosion extends Projectile {
   public Explosion(double x, double y, Player ownerPlayer) {
      damage = 48;
      width = 160;
      height = 160;
      life = 45;
      alive = true;
      knockbackStrength = 69;
      this.owner = ownerPlayer.keyLayout;
      this.ownerPlayer = ownerPlayer;
      dir = 0;
      fileName = "none.png";
      pos = new Vector(x, y);
      velocity = new Vector(0, 0);
      draw = false;
      unreflectable = false;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   @Override
   public void update() {
    SoundEngine.playSound("explosion");
    for (int i = 0; i < 16; i++) {
       battleScreen
             .addParticle(new Particle(pos.x + width / 2 - 16, pos.y + height / 2 - 16, (Math.random() - 0.5) * 10,
                   (Math.random() - 0.5) * 10, 6, 4, 4, "smoke.png"));
       battleScreen
             .addParticle(new Particle(pos.x + width / 2 - 8, pos.y + height / 2 - 8, (Math.random() - 0.5) * 15,
                   (Math.random() - 0.5) * 15, 4, 4, 4, "fire.png"));
    }
    hittingPlayer();
    alive = false;
   }

   @Override
    public void onHitPlayer(Player player) {
        dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
                .rotationOf();
    }

   @Override
   public void kill() {
   }
}