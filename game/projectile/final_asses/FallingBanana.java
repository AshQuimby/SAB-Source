package game.projectile.final_asses;

import game.Player;
import game.SoundEngine;
import game.particle.Particle;
import game.physics.*;
import game.projectile.Projectile;

public class FallingBanana extends Projectile {
    public FallingBanana(double x, double y, double velX, double velY, double kb, double kbDir, int owner,
        Player ownerPlayer) {
        damage = 12;
        width = 44;
        height = 44;
        life = 120;
        alive = true;
        knockbackStrength = kb;
        this.ownerPlayer = ownerPlayer;
        this.owner = owner;
        unreflectable = true;
        unParryable = true;
        dir = kbDir;
        fileName = "falling_banana.png";
        pos = new Vector(x, y);
        unreflectable = false;
        velocity = new Vector(velX, velY);
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        velocity.y += 1;
        hitbox.setPosition(pos);
        if (++frame >= 4) {
            frame = 0;
        }
        boolean hitStage = move(velocity, true);
        if (hitStage) {
            velocity.y *= -0.5;
            velocity.x += (Math.random() - 0.5) * 16;
        }
        if (hitbox.overlaps(ownerPlayer.hitbox)) {
            alive = false;
            ownerPlayer.damage -= 10;
            if (ownerPlayer.damage < 0) {
                ownerPlayer.damage = 0;
            }
            SoundEngine.playSound("chomp");
            for (int i = 0; i < 10; i++) {
                battleScreen.addParticle(new Particle(ownerPlayer.center().x - 4, ownerPlayer.center().y - 4, (Math.random() - 0.5) * 2, (Math.random() - 0.5) * 2, 4, 8, 8, "twinkle.png"));
            }
        }
        hittingPlayer();
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 1;
    }

    @Override
    public void kill() {
    }
}
