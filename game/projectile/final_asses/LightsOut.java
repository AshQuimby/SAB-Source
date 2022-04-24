package game.projectile.final_asses;

import game.Player;
import game.physics.*;
import game.SoundEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import game.projectile.HomingProjectile;

public class LightsOut extends HomingProjectile {

    public LightsOut(Player ownerPlayer) {
        pos = Vector.zero();
        velocity = Vector.zero();
        hitbox = new AABB(0, 0, 0, 0);
        draw = false;
        this.ownerPlayer = ownerPlayer;
        this.owner = ownerPlayer.keyLayout;
        fileName = "none.png";
        alive = true;
        damage = 0;
        unParryable = true;
        unreflectable = true;
        knockbackStrength = 0;
        dir = 0;
        direction = 1;
        life = 500;
        SoundEngine.playMusicOnce("sus");
    }

    @Override
    public void updateAfterTargeting() {
        if (--life == 0) {
            alive = false;
        }
        if (life == 10) {
            for (Player player : battleScreen.getPlayerList()) {
                player.render = true;
            }
            ownerPlayer.hitbox.setCenter(targetPlayer.hitbox.getCenter());
            ownerPlayer.velocity.x *= 0;
            ownerPlayer.velocity.y += 2;
            ownerPlayer.falling = false;
            ownerPlayer.jumps = ownerPlayer.selectedChar.jumps;
            targetPlayer.hitPlayer(48, 52, Utilities.quickKnockbackDirection(ownerPlayer.direction == 1, -30),
                    0.025, this);
        } else if (life > 10) {
            for (Player player : battleScreen.getPlayerList()) {
                player.render = false;
                player.stuck = 1;
            }
        }
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 4;
        dir = new Vector(knockbackStrength, 0).rotateBy(Vector.sub(player.center(), center()).rotationOf())
                .rotationOf();
        SoundEngine.playSound("crunch");
    }

    @Override
    public void kill() {
        SoundEngine.playMusic(battleScreen.getStage().getMusic());
    }

    @Override
    public boolean drawPriority() {
        return true;
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        if (life > 10) {
            g.setColor(Color.BLACK);
            g.fillRect((int) battleScreen.getStage().getUnsafeBlastZone().x,
                    (int) battleScreen.getStage().getUnsafeBlastZone().y,
                    (int) battleScreen.getStage().getUnsafeBlastZone().width,
                    (int) battleScreen.getStage().getUnsafeBlastZone().height);
        }
    }
}