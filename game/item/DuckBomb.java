package game.item;

import game.Player;

import game.physics.*;
import game.projectile.ThrownDuckBomb;

public class DuckBomb extends Item {

    public DuckBomb(Vector position, Player holder) {
        super(position, 44, 44, holder, "duck_bomb_item.png");
    }

    @Override
    public void onUse() {
        holder.battleScreen
                .addProjectileAtCenter(
                        new ThrownDuckBomb(hitbox.getCenterX(), hitbox.getCenterY() - 16, 12 * holder.direction, -5,
                                18, 14, holder.direction == 1 ? Math.toRadians(340) : Math.toRadians(200), holder.keyLayout, holder));
        holder.endLag = 16;
        remove();
    }
}
