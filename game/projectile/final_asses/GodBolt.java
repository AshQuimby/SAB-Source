package game.projectile.final_asses;

import game.Player;
import game.particle.Particle;
import game.physics.*;
import game.projectile.HomingProjectile;

public class GodBolt extends HomingProjectile {
    public GodBolt(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
            Player ownerPlayer) {
        damage = 10;
        width = 56;
        height = 56;
        life = 100;
        alive = true;
        knockbackStrength = kb;
        this.ownerPlayer = ownerPlayer;
        this.owner = owner;
        unreflectable = true;
        unParryable = true;
        dir = kbDir;
        fileName = "god_bolt.png";
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
        if (++frame >= 6) {
            frame = 0;
        }
        if (--life == 0) {
            alive = false;
        }
        Vector targetVelocity = new Vector(24, 0).rotateBy(Vector.sub(targetPlayer.center(), center()).rotationOf());
        velocity = Vector.add(velocity.mul(15), targetVelocity).div(16.0);
        dir = velocity.rotationOf();
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
        for (int i = 0; i < 4; i++) {
            battleScreen.addParticle(new Particle(center().x - 8, center().y - 8, (Math.random() - 0.5) * 4, (Math.random() - 0.5) * 4, 4, 8, 8, "seagull_twinkle.png"));
        }
    }
}