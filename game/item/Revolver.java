package game.item;

import game.Player;
import game.SoundEngine;
import game.physics.*;
import game.projectile.RevolverRound;

public class Revolver extends Item {

    int inUse;
    int uses;
    
    public Revolver(Vector position, Player holder) {
        super(position, 56, 56, holder, "revolver.png");
        inUse = 0;
        uses = 6;
        offset = new Vector(-8, 4);
    }

    @Override
    public void onUse() {
        inUse = 8;
        holder.endLag = 8;
        holder.battleScreen
                .addProjectileAtCenter(
                        new RevolverRound(hitbox.getCenterX() + 14 * holder.direction, hitbox.getCenterY() + 12, 10 * holder.direction, 0,
                                8, holder.direction == 1 ? Math.toRadians(0) : Math.toRadians(180), holder.playerId,
                                holder));
        SoundEngine.playSound("gunshot");
        uses--;
    }

    @Override
    public void update() {
        if (--inUse > 0) {
            frame = 1;
        } else {
            if (uses <= 0) {
               toss();
            }
            frame = 0;
        }
    }
}
