package game.item;

import game.Player;
import game.particle.Particle;
import game.physics.*;
import game.projectile.Projectile;

public class Glasses extends Item {

    int uses;

    public Glasses(Vector position, Player holder) {
        super(position, 32, 16, holder, "glasses.png");
        uses = 16;
    }

    @Override
    public void onUse() {

    }

    @Override
    public void update() {
        for (Projectile projectile : holder.battleScreen.getProjectiles()) {
            if (projectile.ownerPlayer != holder && projectile.hitbox.overlaps(holder.hitbox) && projectile != null) {
                projectile.knockbackStrength *= 0;
                projectile.damage *= 0;
                projectile.alive = !projectile.unParryable;
                projectile.ownerPlayer = holder;
                projectile.owner = holder.keyLayout;
                projectile.hitPlayer = 1;
                holder.invincible = true;
                uses--;
                for (int i = 0; i < 2; i++) {
                    holder.battleScreen.addParticle(new Particle(projectile.pos.x + projectile.width / 2,
                            projectile.pos.y + projectile.height / 2, (Math.random() - 0.5) * 3,
                            (Math.random() - 0.5) * 3, 2, 4, 4, "twinkle.png"));
                }

            }
        }
        if (uses < 0) {
            toss();
        }
    }

    @Override
    public boolean hasUseAction() {
        return false;
    }
}
