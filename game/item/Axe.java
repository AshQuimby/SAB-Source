package game.item;

import game.Player;

import game.physics.*;
import game.projectile.ThrownAxe;

public class Axe extends Item {

    public Axe(Vector position, Player holder) {
        super(position, 48, 48, holder, "axe_item.png");
    }

    @Override
    public void onUse() {
        holder.battleScreen
                .addProjectileAtCenter(
                        new ThrownAxe(hitbox.getCenterX(), hitbox.getCenterY() - 16, 12 * holder.direction, -5,
                                holder.direction == 1 ? Math.toRadians(340) : Math.toRadians(200), holder.keyLayout,
                                holder.direction, holder));
        holder.endLag = 12;
        remove();
    }
}
