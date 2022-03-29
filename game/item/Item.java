package game.item;

import game.Player;
import game.particle.Particle;
import game.physics.*;
import game.Images;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.Serializable;

public class Item implements Serializable {
    public AABB hitbox;
    public Player holder;
    public String fileName;
    public boolean draw;
    public int frame;
    public Vector offset;
    public int life;

    public Item(Vector position, int width, int height, Player holder, String fileName) {
        hitbox = new AABB(position.x, position.y, width, height);
        this.holder = holder;
        this.fileName = fileName;
        draw = true;
        frame = 0;
        offset = new Vector(0, 0);
        life = -1;
    }

    public final void preUpdate() {
        hitbox.setCenter(Vector
                .sub(Vector.sub(holder.center(), new Vector(holder.selectedChar.heldItemOffset.x * holder.direction,
                        holder.selectedChar.heldItemOffset.y)), new Vector(offset.x * holder.direction, offset.y)));
        if (--life == 0) {
            toss();
        }
        update();
    }

    public void update() {

    }

    public boolean hitPlayers(int damage, double knockback, double knockbackDirection) {
        boolean hit = false;
        for (Player player : holder.battleScreen.getPlayerList()) {
            if (player != holder && player.hitbox.overlaps(hitbox) && !player.invincible) {
                player.hitPlayerByItem(damage, knockback, knockbackDirection, 0.01, this);
                hit = true;
            }
        }
        return hit;
    }

    public void onUse() {
    }

    public final void toss() {
        if (holder.endLag < 6) {
            holder.endLag = 6;
        }
        for (int i = 0; i < 4; i++) {
            holder.battleScreen.addParticle(
                    new Particle(hitbox.getCenter().x - 4, hitbox.getCenter().y - 4, (Math.random() - 0.5) * 3,
                            (Math.random() - 0.5) * 3, 2, 4, 4, "smoke.png"));
        }
        holder.heldItem = null;
    }

    public final void remove() {
        holder.heldItem = null;
    }

    public void render(Graphics g, ImageObserver target) {
        BufferedImage image = Images.getImage(fileName);

        if (holder.direction < 0 && draw) {
            holder.battleScreen.renderObject(g, image, hitbox.getPosition(), (int) hitbox.width, (int) hitbox.height,
                    frame, true,
                    target);
        } else if (draw) {
            holder.battleScreen.renderObject(g, image, hitbox.getPosition(), (int) hitbox.width, (int) hitbox.height,
                    frame, false,
                    target);
        }
    }
    
    public boolean hasUseAction() {
        return true;
    }
}
