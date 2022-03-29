package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.GravityParticle;
import game.particle.Particle;

public class Glide extends Projectile {
   public Glide(double x, double y, int owner, Player ownerPlayer) {
      damage = 4;
      width = 96;
      height = 96;
      life = 16;
      alive = true;
      knockbackStrength = 6;
      this.owner = owner;
      this.ownerPlayer = ownerPlayer;
      dir = Math.toRadians(270);
      fileName = "glide.png";
      pos = new Vector(x, y);
      unreflectable = true;
      hitbox = new AABB(pos.x, pos.y, width, height);
   }

   int frameTimer = 0;

   @Override
   public void update() {
      ownerPlayer.frame = 15;
      if (--life <= 0) {
         alive = false;
      }
      ownerPlayer.velocity.y -= 2;
      hitPlayer--;
      knockbackStrength = 6;
      ownerPlayer.velocity.x *= 0.4;
      battleScreen
            .addParticle(new GravityParticle(hitbox.x + width / 2, hitbox.y + height / 2, (Math.random() - 0.5) * 8,
                  (Math.random() - 0.5) * 8, 1, 4, 6, "feather.png", 2));
      for (int i = 0; i < battleScreen.getProjectiles().size(); i++) {
         if (battleScreen.getProjectiles().get(i) != this
               && battleScreen.getProjectiles().get(i).getClass() == this.getClass()
               && battleScreen.getProjectiles().get(i).life < life
               && battleScreen.getProjectiles().get(i).owner == this.owner) {
            battleScreen.getProjectiles().get(i).alive = false;
            Projectile proj = battleScreen.getProjectiles().get(i);
            for (int j = 0; j < 4; j++) {
               battleScreen.addParticle(
                     new Particle(proj.pos.x + width / 2, proj.pos.y + height / 2, (Math.random() - 0.5) * 3,
                           (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
            }
         }
      }
      if (ownerPlayer.touchingStage)
         alive = false;
      hittingPlayer();
      hitbox.setPosition(Vector.sub(ownerPlayer.hitbox.getPosition(), ownerPlayer.selectedChar.offset));
      pos = hitbox.getPosition();
   }

   @Override
   public void onHitPlayer(Player player) {
      for (int j = 0; j < 4; j++) {
      battleScreen.addParticle(new Particle(player.hitbox.x + player.hitbox.width / 2, player.hitbox.y + player.hitbox.height / 2, (Math.random() - 0.5) * 3, (Math.random() - 0.5) * 3, 2, 4, 4, "blood.png"));
      }
      hitPlayer = 2;
   }

   @Override
   public void kill() {
   } // Unused
}