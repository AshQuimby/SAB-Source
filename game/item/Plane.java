package game.item;

import game.Player;

import game.physics.*;
import game.projectile.ThrownPlane;

public class Plane extends Item {

    public Plane(Vector position, Player holder) {
        super(position, 76, 32, holder, "plane_item.png");
        offset = new Vector(0, -8);
    }

    @Override
    public void onUse() {
        holder.battleScreen
                .addProjectileAtCenter(
                        new ThrownPlane(hitbox.getCenterX(), hitbox.getCenterY() - 16, 12 * holder.direction, 0,
                                holder.direction == 1 ? Math.toRadians(0) : Math.toRadians(180), holder.playerId,
                                holder.direction, holder));
        holder.endLag = 4;
        remove();
    }
}
