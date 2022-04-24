package game.projectile.final_asses;

import game.Player;
import game.SoundEngine;
import game.physics.*;
import game.projectile.Projectile;

public class SaxSolo extends Projectile {
    public SaxSolo(double x, double y, int owner, Player ownerPlayer) {
        life = 390;
        alive = true;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        fileName = "none.png";
        unreflectable = true;
        unParryable = true;
        draw = false;
        rotation = 0;
        hitbox = new AABB(pos.x, pos.y, width, height);
        pos = hitbox.getPosition();
    }

    double rotation;

    @Override
    public void update() {
        ownerPlayer.stuck = 1;
        ownerPlayer.frame = 10;
        hitbox.setCenter(ownerPlayer.center());
        ownerPlayer.invincible = true;
        if (life % 5 == 0) {
            for (int i = 0; i < 4; i++) {
                Vector vel = new Vector(16, 0).rotateBy(rotation);
                battleScreen.addProjectileAtCenter(
                        new SaxSoloNote(ownerPlayer.center().x, ownerPlayer.center().y, vel.x, vel.y, 10,
                                vel.rotationOf(),
                                ownerPlayer.keyLayout, ownerPlayer));
                rotation += Math.toRadians(90);
            }
        }
        rotation += Math.toRadians(5);
        if (--life == 0) {
            alive = false;
        }
    }

    @Override
    public void kill() {
        SoundEngine.playMusic(battleScreen.getStage().getMusic());
    }
}
