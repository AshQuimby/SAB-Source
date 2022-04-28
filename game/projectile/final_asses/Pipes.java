package game.projectile.final_asses;

import java.util.ArrayList;
import java.util.List;

import game.Player;
import game.physics.*;
import game.projectile.Projectile;

public class Pipes extends Projectile {
    public Pipes(double x, double y, double kbDir, int owner, int direction,
            Player ownerPlayer) {
        damage = 12;
        width = 64;
        this.direction = -1;
        height = 64;
        life = 160;
        alive = true;
        knockbackStrength = 28;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        dir = kbDir;
        fileName = "pipes.png";
        pos = new Vector(x, y);
        ownerPlayer.render = false;
        velocity = new Vector(0, 0);
        trueDirection = 0;
        cameFrom = 0;
        unreflectable = true;
        unParryable = true;
        hitbox = new AABB(pos.x, pos.y, width, height);
    }

    List<Pipe> pipes = new ArrayList<>();
    int cameFrom;
    int trueDirection;

    @Override
    public void update() {
        if (--life == 0) {
            alive = false;
        }
        pos = ownerPlayer.pos;
        hitbox.setPosition(pos);
        if (ownerPlayer.readableKeysJustPressed[Player.UP] == 1) {
            trueDirection = 0;
        }
        if (ownerPlayer.readableKeysJustPressed[Player.LEFT] == 1) {
            trueDirection = 1;
        }
        if (ownerPlayer.readableKeysJustPressed[Player.DOWN] == 1) {
            trueDirection = 2;
        }
        if (ownerPlayer.readableKeysJustPressed[Player.RIGHT] == 1) {
            trueDirection = 3;
        }
        if (trueDirection == cameFrom) {
            frame = trueDirection * 2;
        }
        if (cameFrom == 0 && trueDirection == 1 || cameFrom == 3 && trueDirection == 2) {
            frame = 1;
        } else if (cameFrom == 0 && trueDirection == 3 || cameFrom == 1 && trueDirection == 2) {
            frame = 3;
        } else if (cameFrom == 2 && trueDirection == 3 || cameFrom == 1 && trueDirection == 0) {
            frame = 5;
        } else if (cameFrom == 2 && trueDirection == 1 || cameFrom == 3 && trueDirection == 0) {
            frame = 7;
        }
        ownerPlayer.stuck = 1;
        ownerPlayer.render = false;
        ownerPlayer.invincible = true;
        if (life % 5 == 0) {
            battleScreen.addProjectile(new Pipe(pos.x, pos.y, owner, direction, frame, ownerPlayer));
            cameFrom = trueDirection;
            if (trueDirection == 0) {
                ownerPlayer.hitbox.y -= 64;
            } else if (trueDirection == 1) {
                ownerPlayer.hitbox.x -= 64;
            } else if (trueDirection == 2) {
                ownerPlayer.hitbox.y += 64;
            } else if (trueDirection == 3) {
                ownerPlayer.hitbox.x += 64;
            }
        }
        hittingPlayer();
        incrementHitPlayer(-1);
    }

    @Override
    public void onHitPlayer(Player player) {
        dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
                .rotationOf();
        hitPlayer(player, 2);
    }

    @Override
    public void kill() {
        ownerPlayer.render = true;
    } // Unused
}