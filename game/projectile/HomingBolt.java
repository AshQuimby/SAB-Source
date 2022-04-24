package game.projectile;

import game.Player;
import game.particle.Particle;
import game.physics.*;
import game.projectile.HomingProjectile;

public class HomingBolt extends Projectile {

    Player target;

    public HomingBolt(double x, double y, double velX, double velY, Player ownerPlayer, Player target) {
        damage = 10;
        width = 32;
        height = 32;
        life = 40;
        alive = true;
        this.target = target;
        knockbackStrength = 8;
        this.ownerPlayer = ownerPlayer;
        owner = ownerPlayer.keyLayout;
        unreflectable = true;
        unParryable = false;
        dir = 0;
        fileName = "mini_bolt.png";
        pos = new Vector(x, y);
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
        Vector targetVelocity = new Vector(64, 0).rotateBy(Vector.sub(target.center(), center()).rotationOf());
        velocity = Vector.add(velocity.mul(31), targetVelocity).div(32.0);
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
            battleScreen.addParticle(new Particle(center().x - 8, center().y - 8, (Math.random() - 0.5) * 4, (Math.random() - 0.5) * 4, 1, 8, 8, "seagull_twinkle.png"));
        }
    }
}