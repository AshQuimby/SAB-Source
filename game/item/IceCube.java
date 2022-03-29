package game.item;

import game.Player;

import game.physics.*;
import game.projectile.ThrownIceCube;

public class IceCube extends Item {

    public IceCube(Vector position, Player holder) {
        super(position, 40, 44, holder, "ice_cube_item.png");
    }

    @Override
    public void onUse() {
        holder.battleScreen
                .addProjectileAtCenter(
                        new ThrownIceCube(hitbox.getCenterX(), hitbox.getCenterY() - 16, 12 * holder.direction, -5,
                                18, 14, holder.direction == 1 ? Math.toRadians(340) : Math.toRadians(200), holder.keyLayout, holder));
        holder.endLag = 12;
        remove();
    }
}
