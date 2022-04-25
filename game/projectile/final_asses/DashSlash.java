package game.projectile.final_asses;

import game.Images;
import game.Player;
import game.physics.*;
import game.projectile.*;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class DashSlash extends Projectile {
    Vector backToPos;
    public DashSlash(Player ownerPlayer) {
        damage = 32;
        width = 128;
        this.direction = ownerPlayer.direction;
        height = 128;
        life = 15;
        alive = true;
        knockbackStrength = 58;
        this.owner = ownerPlayer.playerId;
        this.ownerPlayer = ownerPlayer;
        dir = 0;
        if (ownerPlayer.costume > 0)
            fileName = "dash_slash_alt_" + ownerPlayer.costume + ".png";
        else
            fileName = "dash_slash.png";
        pos = new Vector(ownerPlayer.hitbox.x, ownerPlayer.hitbox.y);
        backToPos = pos.clone();
        velocity = new Vector(0.2 * direction, 0);
        unreflectable = true;
        unParryable = true;
        updatesPerTick = 2;
        hitbox = new AABB(pos.x, pos.y, width, height);
        hitbox.setCenter(ownerPlayer.center());
        pos = hitbox.getPosition();
        oldPos.add(pos);
        oldFrame.add(0);
    }

    int frameTimer = 0;

    @Override
    public void update() {
        ownerPlayer.move(new Vector(velocity.x, 0), true);
        hitbox.setCenter(ownerPlayer.center());
        if (--life >= 0) {     
            pos = hitbox.getPosition();
            ownerPlayer.stuck = 1;
            if (Math.abs(velocity.x) < 24)
                velocity.x *= 3;
            if (frame < 2 && life % 2 == 0) {
                frame++;
            }
            if (frame == 2 && life == 5) {
                frame++;
            }
            dir = direction == 1 ? Math.toRadians(-30) : Math.toRadians(-150); 
            oldPos.add(pos);
            oldFrame.add(frame);
            ownerPlayer.invincible = true;
            hittingPlayer();
        } else {
            velocity.x *= 0.4;
            ownerPlayer.stuck = 1;
            if (Math.abs(life) % 3 == 0) {
                frame++;
            }
            if (life < -10) {
                ownerPlayer.hitbox.setPosition(backToPos);
                battleScreen.addProjectile(new Teleport(ownerPlayer.hitbox.x, -10000, 0, 0,
                  Math.toRadians(200) + ((ownerPlayer.direction + 1) / 2) * Math.toRadians(140),
                  ownerPlayer.playerId, ownerPlayer.direction, ownerPlayer));
                alive = false;
            }
        }
        if (oldFrame.size() > 4) {
            oldFrame.remove(0);
            oldPos.remove(0);
        }
    }

    public ArrayList<Vector> oldPos = new ArrayList<Vector>();
    public ArrayList<Integer> oldFrame = new ArrayList<Integer>();

    @Override
    public void render(Graphics g, ImageObserver target) {
        BufferedImage image = Images.getImage(fileName);

        for (int i = 0; i < oldPos.size(); i++) {
            if (direction < 0 && draw) {
                battleScreen.renderObject(g, image, oldPos.get(i), width, height, oldFrame.get(i), true, target);
            } else if (draw) {
                battleScreen.renderObject(g, image, oldPos.get(i), width, height, oldFrame.get(i), false, target);
            }
        }
        postRender(g, target);
    }

    @Override
    public void kill() {
    }
}
