package game.projectile;

import game.Player;
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
            hitPlayer = 0;
            knockbackStrength = 48;
            damage = 27;
            hitbox.transformDimensions(width * 2, height);
            hittingPlayer();
            alive = false;
        } else {
            knockbackStrength = 8;
            hittingPlayer();
        }

        if (ownerPlayer.grabbingLedge) {
            alive = false;
        }
    }

    @Override
    public void onHitPlayer(Player player) {
        if (!ownerPlayer.touchingStage) {
            hitPlayer = 6;
            dir = Math.toRadians(90);
        }

        dir = Vector.sub(player.center().add(0, -height), center().add(0, -40)).rotationOf();
    }

    @Override
    public void kill() {
    } // Unused
}