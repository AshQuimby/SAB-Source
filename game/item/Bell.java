package game.item;

import game.Player;
import game.SoundEngine;
import game.physics.*;
import game.projectile.final_asses.Bong;

public class Bell extends Item {

    int swung;
    int uses;
    boolean hitPlayer;

    public Bell(Vector position, Player holder) {
        super(position, 32, 24, holder, "bell.png");
        swung = 0;
        uses = 25;
        offset = new Vector(0, -16);
        hitPlayer = false;
    }

    @Override
    public void onUse() {
        if (swung <= 0) {
           swung = 8;
           uses--;
        }
    }

    @Override
    public void update() {
        if (--swung == 0) {
            if (uses <= 0) {
                toss();
            }
            frame = 0;
        }
        if (swung == 7) {
            SoundEngine.playSound("bell");
            holder.battleScreen
                .addProjectileAtCenter(
                        new Bong(hitbox.getCenterX(), hitbox.getCenterY(), 0, holder.keyLayout, holder.direction, holder));
            holder.endLag = 8;
        }
        if (swung > 0 && swung % 2 == 0)
            frame++;
    }
}
