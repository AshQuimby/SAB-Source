package game.projectile;

import game.Player;
import game.physics.*;
import game.particle.Particle;

public class Grab extends Projectile {
    public Grab(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 8;
        width = 48;
        this.direction = direction;
        height = 16;
        life = 8;
        alive = true;
        knockbackStrength = 0;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "none.png";
        draw = false;
        pos = new Vector(x, y);
        velocity = new Vector(velX, velY);
        unreflectable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        if (--life == 0) {
            alive = false;
        }
        pos.x = ownerPlayer.center().x - width / 2 + (24 * ownerPlayer.direction);
        pos.y = ownerPlayer.center().y - 32;
        hitbox.setPosition(pos);
        if (hittingPlayer() != null) {
            for (Player player : hittingPlayer()) {
                if (player.playerId != owner && !player.invincible) {
                    player.hitPlayer(damage, 0, 0, 0, this, false);
                    player.velocity = Vector.sub(ownerPlayer.center(), player.center());
                    player.velocity.y -= 16;
                    player.touchingStage = false;
                    ownerPlayer.velocity = Vector.sub(ownerPlayer.center(), player.center()).mul(0.4);
                    ownerPlayer.velocity.y -= 16 * 0.4;
                    ownerPlayer.touchingStage = false;
                    for (int i = 0; i < 4; i++) {
                        battleScreen.addParticle(new Particle(player.pos.x + player.width / 2,
                                player.pos.y + player.height / 2, (Math.random() - 0.5) * 3,
                                (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
                    }
                }
            }
            setHitPlayer(1);
        }
    }

    @Override
    public boolean overrideHitPlayer() {
        return true;
    }

    @Override
    public void kill() {
    } // Unused
}