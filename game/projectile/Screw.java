package game.projectile;

import game.Player;
import game.character.EmptySoldier;
import game.physics.*;
import java.util.List;
import game.particle.Particle;

public class Screw extends Projectile {
    public Screw(double x, double y, double velX, double velY, double kb, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 8;
        width = 86;
        this.direction = direction;
        height = 32;
        life = 12;
        alive = true;
        knockbackStrength = 20;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "none.png";
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
        pos.x = ownerPlayer.center().x + 32 * ownerPlayer.direction - width / 2;
        pos.y = ownerPlayer.center().y - 12;
        hitbox.setPosition(pos);
        hittingPlayer();
    }

    @Override
    public void onHitPlayer(Player player) {
        if (ownerPlayer.selectedChar.getClass() == EmptySoldier.class) {
            EmptySoldier emptySoldier = ((EmptySoldier) ownerPlayer.selectedChar);

            emptySoldier.spirit += 11;
            emptySoldier.spirit = Math.min(emptySoldier.spirit, 100);
        }

        alive = false;
    }

    @Override
    public void kill() {
    } // Unused
}