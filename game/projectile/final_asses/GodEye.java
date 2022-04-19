package game.projectile.final_asses;

import game.Player;
import game.particle.Particle;
import game.physics.*;
import game.projectile.HomingProjectile;

public class GodEye extends HomingProjectile {
    public GodEye(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
            Player ownerPlayer) {
        damage = 24;
        width = 72;
        height = 72;
        life = 40;
        alive = true;
        knockbackStrength = kb;
        this.ownerPlayer = ownerPlayer;
        this.owner = owner;
        unreflectable = true;
        unParryable = true;
        dir = kbDir;
        fileName = "eye_of_god.png";
        pos = new Vector(x, y);
        unreflectable = false;
        velocity = new Vector(velX, velY);
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public boolean drawPriority() {
        return true;
    }

    @Override
    public void updateAfterTargeting() {
        if (--life == 0) {
            alive = false;
        }
        if (life % 2 == 0) {
            if (++frame >= 4) {
                frame = 0;
            }
        }
        velocity = velocity.mul(0.9);
        move(velocity, false);
        hitbox.setPosition(pos);
        hittingPlayer();
    }

    @Override
    public void onHitPlayer(Player player) {
        alive = false;
    }

    @Override
    public void kill() {
        int rot = 0;
        for (int i = 0; i < 12; i++) {
            Vector vel = new Vector(16, 0).rotateBy(Math.toRadians(rot));
            battleScreen.addProjectileAtCenter(
                    new MiniGodBolt(center().x, center().y, vel.x,
                            vel.y, 8, 0, owner, ownerPlayer));
            rot += 30;
        }
        for (int i = 0; i < 8; i++) {
            battleScreen.addParticle(new Particle(center().x - 6, center().y - 6, (Math.random() - 0.5) * 8, (Math.random() - 0.5) * 8, 3, 8, 8, "seagull_twinkle.png"));
        }
    }
}