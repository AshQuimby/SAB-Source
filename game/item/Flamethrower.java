package game.item;

import game.Player;
import game.SoundEngine;
import game.physics.*;
import game.projectile.Flame;

public class Flamethrower extends Item {

    int inUse;
    int uses;

    public Flamethrower(Vector position, Player holder) {
        super(position, 120, 24, holder, "flamethrower.png");
        inUse = 0;
        uses = 20;
        offset = new Vector(-24, -4);
    }

    @Override
    public void onUse() {
        inUse = 2;
        holder.battleScreen
                .addProjectileAtCenter(
                        new Flame(hitbox.getCenterX() + 64 * holder.direction, hitbox.getCenterY() + 12, 0, 0,
                                4, holder.direction == 1 ? Math.toRadians(0) : Math.toRadians(180), holder.keyLayout,
                                holder));
        SoundEngine.playSound("fire");
        uses--;
    }

    @Override
    public void update() {
        if (++frame >= 4) {
            frame = 0;
        }
        if (--inUse == 0) {
            if (holder.readableKeys[Player.ATTACK]) {
                holder.heldItem.onUse();
            }
            if (uses <= 0) {
                toss();
            }
        }
    }
}
