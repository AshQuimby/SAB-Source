package game.projectile.final_asses;

import game.Player;
import game.SoundEngine;
import game.physics.*;
import game.projectile.Projectile;
import game.stage.Platform;

public class GodSeagullLeg extends Projectile {
    private boolean hitGround;

    public GodSeagullLeg(int owner, int direction, Player ownerPlayer) {
        width = 392;
        this.direction = direction;
        height = 704;
        life = 800;
        alive = true;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        frame = 0;
        fileName = "big_seagull_leg_with_body.png";
        unreflectable = true;
        unParryable = true;

        double x = 0;
        // If the leg is the left leg, stand on the leftmost platform. If it's the right leg, stand on the rightmost.
        if (direction == -1) {
            Platform leftmostPlatform = null;

            for (Platform platform : ownerPlayer.battleScreen.getStage().getPlatforms()) {
                if (leftmostPlatform == null || platform.getHitbox().getCenterX() < leftmostPlatform.getHitbox().getCenterX()) {
                    leftmostPlatform = platform;
                }
            }

            x = leftmostPlatform.getHitbox().x - width / 2;
        } else if (direction == 1) {
            Platform rightmostPlatform = null;

            for (Platform platform : ownerPlayer.battleScreen.getStage().getPlatforms()) {
                if (rightmostPlatform == null || platform.getHitbox().getCenterX() > rightmostPlatform.getHitbox().getCenterX()) {
                    rightmostPlatform = platform;
                }
            }

            x = rightmostPlatform.getHitbox().getX2() - width + width / 2;
        }

        pos = new Vector(x, ownerPlayer.battleScreen.getStage().getSafeBlastZone().y - height);
        velocity = new Vector(0, 0);
        hitbox = new AABB(pos.x, pos.y, width, height);

        hitGround = false;
    }

    @Override
    public void update() {
        if (--life <= 0) alive = false;

        if (life > 200) {
            pos = new Vector(hitbox.x, hitbox.y);
            boolean justHitGround = move(velocity, true);
            velocity.y += 2;
            hitbox.setPosition(pos);
            pos = new Vector(hitbox.x, hitbox.y + 12);

            if (justHitGround && !hitGround) {
                SoundEngine.playSound("explosion");
                ownerPlayer.battleScreen.cameraShake(25);
                hitGround = true;
            }

            if (hitGround) {
                velocity.y = 0;
            }
        } else {
            velocity.y -= 1;
            move(velocity, false);
        }
    }

    @Override
    public void onHitPlayer(Player player) {
    }

    @Override
    public void kill() {
    }
}
