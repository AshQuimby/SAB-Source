package game.projectile.final_asses;

import game.Player;
import game.physics.*;
import game.SoundEngine;
import game.projectile.Projectile;

public class Pipe extends Projectile {
    public Pipe(double x, double y, int owner, int direction,
            int type, Player ownerPlayer) {
        damage = 12;
        width = 64;
        this.direction = -1;
        height = 64;
        life = 40;
        alive = true;
        knockbackStrength = 12;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = 0;
        frame = type;
        fileName = "pipes.png";
        pos = new Vector(x, y);
        velocity = new Vector(0, 0);
        unreflectable = true;
        unParryable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    @Override
    public void update() {
        if (--life == 0) {
            alive = false;
        }
        ownerPlayer.iFrames = 1;
        ownerPlayer.invincible = true;
        hittingPlayer();
        incrementHitPlayer(-1);
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer(player, 4);
        dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
                .rotationOf();
        SoundEngine.playSound("crunch");
    }

    @Override
    public void kill() {
    } // Unused
}