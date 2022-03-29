package game.item;

import game.Player;
import game.SoundEngine;
import game.particle.Particle;
import game.physics.*;
import game.projectile.Projectile;

public class RubbishLid extends Item {

    int uses;
    int blockTime;
    boolean hitPlayer;

    public RubbishLid(Vector position, Player holder) {
        super(position, 32, 60, holder, "rubbish_bin_lid.png");
        uses = 10;
        blockTime = 0;
        hitPlayer = false;
    }

    @Override
    public void onUse() {
        holder.endLag = 4;
        blockTime = 2;
        SoundEngine.playSound("swish");
        hitbox.x += 8 * holder.direction;
        uses--;
        hitPlayers(4, 12, holder.direction == 1 ? Math.toRadians(320) : Math.toRadians(220));
    }

    @Override
    public void update() {
        for (Projectile projectile : holder.battleScreen.getProjectiles()) {
            if (projectile.ownerPlayer != holder && projectile.hitbox.overlaps(hitbox) && !projectile.unreflectable && projectile != null) {
                projectile.velocity.x *= -1;
                projectile.dir = projectile.velocity.rotationOf();
                projectile.ownerPlayer = holder;
                uses--;
                if (blockTime > 0) {
                   projectile.damage = (int) (projectile.damage * 2);
                   projectile.knockbackStrength *= 2;
                }
                SoundEngine.playSound("shield_bounce");
                for (int i = 0; i < 4; i++) {
                    holder.battleScreen.addParticle(new Particle(projectile.pos.x + projectile.width / 2,
                            projectile.pos.y + projectile.height / 2, (Math.random() - 0.5) * 3,
                            (Math.random() - 0.5) * 3, 2, 4, 4, "twinkle.png"));
                }
            }
        }
        if (blockTime > 0) {
            blockTime--;
        }
        if (uses < 0) {
            toss();
        }
    }
}
