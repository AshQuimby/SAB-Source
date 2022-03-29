package game.item;

import game.Player;
import game.SoundEngine;
import game.physics.*;

public class Rake extends Item {

    int swung;
    int uses;

    public Rake(Vector position, Player holder) {
        super(position, 96, 96, holder, "rake_item.png");
        swung = 0;
        uses = 10;
    }

    @Override
    public void onUse() {
        swung = 8;
        holder.endLag = 12;
        uses--;
    }

    @Override
    public void update() {
        if (--swung == 0) {
            if (uses <= 0) {
                toss();
            }
            frame = 0;
        }
        if (swung == 7 || swung == 6) {
            SoundEngine.playSound("swish");
            hitPlayers(14, 18, holder.direction == 1 ? Math.toRadians(330) : Math.toRadians(210));
        }
        if (swung > 2) {
            frame++;
        }
    }
}
