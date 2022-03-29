package game.item;

import game.Player;
import game.SoundEngine;
import game.physics.*;

public class DuckKnife extends Item {

    int swung;
    int uses;
    boolean hitPlayer;

    public DuckKnife(Vector position, Player holder) {
        super(position, 72, 92, holder, "knife_item.png");
        swung = 0;
        uses = 20;
        hitPlayer = false;
    }

    @Override
    public void onUse() {
        if (swung <= 0) {
           swung = 6;
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
        if (swung == 2 || swung == 1 && !hitPlayer) {
            SoundEngine.playSound("swish");
            hitPlayer = hitPlayers(12, 14, holder.direction == 1 ? Math.toRadians(340) : Math.toRadians(200));
        }
        if (swung > 0)
            frame++;
    }
}
