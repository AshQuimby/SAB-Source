package game.projectile;

import game.Player;
import game.physics.*;
import game.SoundEngine;
import game.particle.GravityParticle;
import game.stage.Platform;

public class Slam extends Projectile {
    public Slam(double x, double y, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 2;
        width = 100;
        this.direction = direction;
        height = 100;
        life = 8;
        alive = true;
        knockbackStrength = kb;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "none.png";
        draw = false;
        pos = new Vector(x, y);
        velocity = new Vector(0, 0);
        unreflectable = true;
        timeUp = 20;
        ownerPlayer.velocity.y = -48;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    int timeUp;

    public boolean staticKnockback() {
        return timeUp > 0;
    }

    @Override
    public void update() {
        dir = Math.toRadians(90);
        hitbox.setCenter(ownerPlayer.center());
        if (timeUp > 0) {
            ownerPlayer.frame = 7;
            ownerPlayer.velocity.x *= 0.;
            ownerPlayer.velocity.y -= 4;
            hitPlayer--;
        } else {
            damage = (int) ownerPlayer.velocity.y;
            damage = Math.min(damage, 48);
            ownerPlayer.frame = 8;
            knockbackStrength = 42;
            ownerPlayer.velocity.x *= 0.8;
            ownerPlayer.velocity.y += 4;
        }

        if (ownerPlayer.touchingStage && alive) {
            battleScreen.cameraShake(20);
            for (Platform platform : battleScreen.getStage().getPlatformCollisions(hitbox)) {
               for (int j = 0; j < 8; j++) {
                  battleScreen.addParticle(new GravityParticle(ownerPlayer.hitbox.x + ownerPlayer.width / 2 - 2,
                        ownerPlayer.hitbox.getY2() - 4, (Math.random() - 0.5) * 16,
                        (Math.random() - 0.5) * 8 - 8, 1, 4, 4, platform.getImage(), 1));
               }
            }
            SoundEngine.playSound("crash");
        }
        
        if (ownerPlayer.readableKeysJustPressed[Player.ATTACK] == 1 && timeUp > 0) {
            timeUp = -1;
            ownerPlayer.velocity.y *= 0.5;
        }
        
        if (--timeUp < -40 || ownerPlayer.touchingStage) {
            alive = false;
        }
        if (timeUp < 0 && ownerPlayer.velocity.y > 10)
            hittingPlayer();
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 8;
    }

    @Override
    public void kill() {
    } // Unused
}