package game.projectile.final_asses;

import game.Player;
import game.SoundEngine;
import game.physics.*;
import game.projectile.Projectile;

public class GodSeagullBeak extends Projectile {
    private boolean pecking;
    private AABB tip;

    public GodSeagullBeak(int owner, Player ownerPlayer) {
        width = 124;
        direction = -1;
        height = 1024;
        life = 800;
        alive = true;
        this.owner = owner;
        this.ownerPlayer = ownerPlayer;
        frame = 0;
        damage = 50;
        knockbackStrength = 24;
        fileName = "god_seagull_beak.png";
        unreflectable = true;
        unParryable = true;
        dir = Math.toRadians(90);
        pos = new Vector(ownerPlayer.center().x, ownerPlayer.battleScreen.getStage().getSafeBlastZone().y - height);
        velocity = new Vector(0, 0);
        hitbox = new AABB(pos.x, pos.y, width, height);
        tip = new AABB(pos.x, pos.y + height - 32, width, 32);

        pecking = false;
    }

    @Override
    public boolean drawPriority() {
        return true;
    }

    @Override
    public void update() {
        if (--life <= 0)
            alive = false;

        if (life > 300) {
            pos = new Vector(hitbox.x, hitbox.y);

            if (pecking) {
                velocity.y += 5;
                AABB originalHitbox = hitbox;
                hitbox = tip;
                hittingPlayer();
                hitbox = originalHitbox;
                boolean justHitGround = move(velocity, true);

                if (justHitGround) {
                    pecking = false;
                    SoundEngine.playSound("crunch");
                    velocity.y = 0;
                    battleScreen.cameraShake(20);
                }

                if (hitbox.getY2() > battleScreen.getStageBounds().getY2()) {
                    pecking = false;
                }
            } else {
                hitPlayer = 0;
                if (pos.y > battleScreen.getStageBounds().y) {
                    velocity.y -= 2;
                } else if (life < 760) {
                    velocity.y = (pos.y - battleScreen.getStageBounds().y) / 10;
                }
                if (ownerPlayer.readableKeysJustPressed[Player.ATTACK] == 1)
                    pecking = true;
                move(velocity, true);
            }
            if (life < 760) {
                if (pos.y < battleScreen.getStageBounds().y - height + 240) {
                    pos.y = battleScreen.getStageBounds().y - height + 240;
                    velocity.y = 0;
                }
            } else {
                if (pos.y < battleScreen.getStageBounds().y - height + 240)
                    velocity.y += 2;
                else
                    velocity.y = 0;
            }

            if (!hitbox.overlaps(battleScreen.getStageBounds())) {
                velocity.x *= -1.1;
            }
            
            velocity.x *= 0.96;

            hitbox.setPosition(pos);
            tip.setPosition(Vector.add(pos, new Vector(0, height - 32)));

            float xAccel = 0;
            if (ownerPlayer.readableKeys[Player.LEFT])
                xAccel--;
            if (ownerPlayer.readableKeys[Player.RIGHT])
                xAccel++;
            velocity.x += xAccel;
        } else {
            velocity.y -= 1;
            move(velocity, false);
        }
    }

    @Override
    public void onHitPlayer(Player player) {
        hitPlayer = 1;
    }

    @Override
    public void kill() {
    }
}
