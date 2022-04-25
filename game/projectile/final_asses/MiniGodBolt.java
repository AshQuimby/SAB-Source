package game.projectile.final_asses;

import game.Player;
import game.particle.Particle;
import game.physics.*;
import game.projectile.Projectile;

public class MiniGodBolt extends Projectile {
    public MiniGodBolt(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
            Player ownerPlayer) {
        damage = 10;
        width = 32;
        height = 32;
        life = 40;
        alive = true;
        knockbackStrength = kb;
        this.ownerPlayer = ownerPlayer;
        this.owner = owner;
        dir = kbDir;
        fileName = "mini_bolt.png";
        pos = new Vector(x, y);
        unreflectable = true;
        unParryable = false;
        velocity = new Vector(velX, velY);
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public boolean drawPriority() {
        return true;
    }

    @Override
    public void update() {
        if (--life == 0) {
            alive = false;
        }
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
            battleScreen.addParticle(new Particle(center().x - 4, center().y - 4, (Math.random() - 0.5) * 4, (Math.random() - 0.5) * 4, 2, 8, 8, "seagull_twinkle.png"));
        }
    }
}