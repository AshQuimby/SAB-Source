package game.projectile;

import game.Player;
import game.particle.AnimatedParticle;
import game.physics.*;

public class ShadowPlunge extends Projectile {
    public ShadowPlunge(double x, double y, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 6;
        width = 128;
        this.direction = direction;
        height = 128;
        life = 1;
        alive = true;
        knockbackStrength = kb;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "shadow_plunge.png";
        draw = true;
        pos = new Vector(x, y);
        velocity = new Vector(0, 0);
        unreflectable = true;
        ownerPlayer.velocity.y = 36;
        hitbox = new AABB(pos.x, pos.y, width / 2, height);
    }

    private void spawnShadowlingParticle() {
        battleScreen.addParticle(new AnimatedParticle(
            center().x,
            center().y + 32,
            (Math.random() - .5) * 8 * (alive ? 1 : 2),
            -Math.random() * 2 - 4,
            1,
            32,
            32,
            Math.random() > 0.5 ? "shadowling_particle.png" : "shadowling_particle_flipped.png",
            6, 
            7
            ));
    }

    @Override
    public void update() {
        dir = Math.toRadians(90);
        ownerPlayer.endLag = 6;
        hitbox.setCenter(ownerPlayer.center());
        pos = hitbox.getCenter();
        pos.x -= width / 2;
        pos.y -= height / 2;

        if (++frame >= 4) {
            frame = 0;
        }

        if (ownerPlayer.touchingStage && alive) {
            battleScreen.cameraShake(12);
                knockbackStrength = 36;
            damage = 24;
            hitbox.transformDimensions(width * 2, height / 2);
            hittingPlayer();

            alive = false;
            for (int i = 0; i < 10; i++) {
                spawnShadowlingParticle();
            }
        } else {
            knockbackStrength = 8;
            hittingPlayer();
        }

        if (ownerPlayer.grabbingLedge || ownerPlayer.tookDamage || ownerPlayer.frozen > 0) {
            alive = false;
        }

        spawnShadowlingParticle();
    }

    @Override
    public void onHitPlayer(Player player) {
        if (!ownerPlayer.touchingStage) {
            hitPlayer(player, 6);
            dir = Math.toRadians(90);
        }

        dir = Vector.sub(player.center().add(0, -height), center().add(0, -40)).rotationOf();
    }

    @Override
    public void kill() {
    } // Unused
}