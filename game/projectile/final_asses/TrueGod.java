package game.projectile.final_asses;

import game.Deity;
import game.Images;
import game.Player;
import game.physics.*;
import game.projectile.HomingProjectile;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class TrueGod extends HomingProjectile implements Deity {
    private int legSpawnDelay;
    private int cloudTime;
    private int wingsFrame;
    private int heartFrame;
    private AABB heart;
    private Vector heartVelocity;
    private Vector targetPosition;
    private int costumeBefore;


    private List<Vector> previousHeartPositions;

    boolean firstLegToSpawn = Math.random() > .5;

    public TrueGod(Player ownerPlayer) {
        life = 800;
        alive = true;
        this.owner = ownerPlayer.playerId;
        this.ownerPlayer = ownerPlayer;
        fileName = "none.png";
        unreflectable = true;
        unParryable = true;
        hitbox = new AABB(666, 666, 666, 666);
        pos = hitbox.getPosition();

        cloudTime = 0;
        heartFrame = 0;
        wingsFrame = 0;
        costumeBefore = ownerPlayer.costume;
        legSpawnDelay = 0;
        previousHeartPositions = new ArrayList<>(5);
    }

    @Override
    public void updateAfterTargeting() {
        if (life == 800) {
            heart = new AABB(0, 0, 620, 620);
            heart.setCenter(battleScreen.getStageBounds().getCenter());
            heart.y -= battleScreen.getStageBounds().height * 2;
            targetPosition = battleScreen.getStageBounds().getCenter();
            heartVelocity = new Vector(0, 0);
        }

        ownerPlayer.invincible = true;
        ownerPlayer.stuck = 1;
        if (ownerPlayer.hitbox.y < battleScreen.getStageBounds().y - 500) {
            ownerPlayer.hitbox.y = battleScreen.getStageBounds().y - 500;
            ownerPlayer.hitbox.setCenterX(battleScreen.getStageBounds().getCenterX());
            ownerPlayer.velocity.x = 0;
            ownerPlayer.velocity.y = 0;
        }
        if (life < 80) {
            ownerPlayer.frame = 13;
            ownerPlayer.hitbox.y += 10;
            ownerPlayer.selectedChar.fileName = "big_seagull.png";
            ownerPlayer.costume = costumeBefore;
            heartVelocity.y -= 5;
        } else {
            ownerPlayer.frame = 15;
            ownerPlayer.velocity.y -= 4;
            if (life < 700) {
                ownerPlayer.costume = 0;
                ownerPlayer.selectedChar.fileName = "true_god.png";
            }
            ownerPlayer.hitbox.translate(ownerPlayer.velocity);
            heartVelocity = Vector.sub(targetPosition, heart.getCenter()).div(20);
        }
        heart.setPosition(Vector.add(heart.getPosition(), heartVelocity));
        if (Vector.distanceBetween(heart.getCenter(), targetPosition) < 30) {
            Vector toTarget = Vector.normalize(Vector.sub(targetPlayer.center(), heart.getCenter())).mul(24);

            if (Math.random() > 0.5) {
                battleScreen.addProjectileAtCenter(new GodBolt(heart.getCenter().x, heart.getCenter().y, toTarget.x,
                        toTarget.y, 16, 0, owner, ownerPlayer));
            } else {
                battleScreen.addProjectileAtCenter(new GodEye(heart.getCenter().x, heart.getCenter().y, toTarget.x,
                        toTarget.y, 4, 0, owner, ownerPlayer));
            }
            targetPosition = battleScreen.getStageBounds().getRandomPoint();
        }

        legSpawnDelay++;
        if (legSpawnDelay == 50) {
            battleScreen.addProjectile(new GodSeagullLeg(ownerPlayer.playerId, firstLegToSpawn ? 1 : -1, ownerPlayer));
        }
        if (legSpawnDelay == 70) {
            battleScreen.addProjectile(new GodSeagullLeg(ownerPlayer.playerId, firstLegToSpawn ? -1 : 1, ownerPlayer));
        }

        if (legSpawnDelay == 80) {
            battleScreen.addProjectile(new GodSeagullBeak(ownerPlayer.playerId, ownerPlayer));
        }

        if (++heartFrame >= 666 / 111) {
            heartFrame = 0;
        }

        if (++wingsFrame >= 15) {
            wingsFrame = 0;
        }

        if (--life == 0) {
            alive = false;
        }
        cloudTime++;

        previousHeartPositions.add(new Vector(heart.x, heart.y));

        battleScreen.cameraShake(1);
    }

    @Override
    public void kill() {
        PRAISE();
    }

    @Override
    public void preRender(Graphics g, ImageObserver target) {
        battleScreen.renderObject(g, Images.getImage("seagull_dust_background.png"),
                new Vector(battleScreen.getStageBounds().x + cloudTime / 3,
                        battleScreen.getStageBounds().y - 48
                                - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                - (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.cos(cloudTime / 24.0) * 10 - 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
        battleScreen.renderObject(g, Images.getImage("seagull_dust_background.png"),
                new Vector(battleScreen.getStageBounds().x + cloudTime / 3 - battleScreen.getStageBounds().width,
                        battleScreen.getStageBounds().y - 48
                                - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                - (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.cos(cloudTime / 24.0) * 10 - 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);

        battleScreen.renderObject(g, Images.getImage("blood_mist_background.png"),
                new Vector(battleScreen.getStageBounds().x - cloudTime / 1.5,
                        battleScreen.getStageBounds().y - 48
                                + (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                + (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.sin(cloudTime / 24.0) * 10 + 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
        battleScreen.renderObject(g, Images.getImage("blood_mist_background.png"),
                new Vector(battleScreen.getStageBounds().x - cloudTime / 1.5 + battleScreen.getStageBounds().width,
                        battleScreen.getStageBounds().y - 48
                                + (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                + (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.sin(cloudTime / 24.0) * 10 + 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        if (heart != null) {
            for (Vector shadowPos : previousHeartPositions) {
                battleScreen.renderObject(g, Images.getImage("heartshadow.png"), shadowPos, 620, 620, heartFrame, false,
                        target);
            }

            battleScreen.renderObject(g, Images.getImage("true_wings.png"),
                    Vector.add(heart.getPosition(), new Vector(-980 / 2, -980 / 2 - 288)), 1600, 1600, false,
                    target);

            battleScreen.renderObject(g, Images.getImage("golden_wing.png"),
                    Vector.add(heart.getPosition(), new Vector(-980 / 2, -980 / 2 - 288)), 1600, 1600, false,
                    target);

            battleScreen.renderObject(g, Images.getImage("ultimate_grace.png"),
                    Vector.add(heart.getPosition(), new Vector(-980 / 2, -980 / 2)), 1600, 1600, false,
                    target);

            battleScreen.renderObject(g, Images.getImage("god_seagull_wing.png"),
                    Vector.add(heart.getCenter(), new Vector(-256, -128)), 256, 256, wingsFrame, false, target);
            battleScreen.renderObject(g, Images.getImage("god_seagull_wing.png"),
                    Vector.add(heart.getCenter(), new Vector(0, -128)), 256, 256, wingsFrame, true, target);

            renderHeart(g, target);
        }
    }

    @Override
    public void lateRender(Graphics g, ImageObserver target) {
        battleScreen.renderObject(g, Images.getImage("seagull_dust.png"),
                new Vector(battleScreen.getStageBounds().x + cloudTime * 2,
                        battleScreen.getStageBounds().y - 48
                                - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                - (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.sin(cloudTime / 24.0) * 10 - 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
        battleScreen.renderObject(g, Images.getImage("seagull_dust.png"),
                new Vector(battleScreen.getStageBounds().x + cloudTime * 2 - battleScreen.getStageBounds().width,
                        battleScreen.getStageBounds().y - 48
                                - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                - (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.sin(cloudTime / 24.0) * 10 - 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
        battleScreen.renderObject(g, Images.getImage("seagull_dust.png"),
                new Vector(battleScreen.getStageBounds().x + cloudTime * 2 - battleScreen.getStageBounds().width * 2,
                        battleScreen.getStageBounds().y - 48
                                - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                - (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.sin(cloudTime / 24.0) * 10 - 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);

        battleScreen.renderObject(g, Images.getImage("blood_mist.png"),
                new Vector(battleScreen.getStageBounds().x - cloudTime * 4,
                        battleScreen.getStageBounds().y - 48
                                + (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                + (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.cos(cloudTime / 24.0) * 10 + 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
        battleScreen.renderObject(g, Images.getImage("blood_mist.png"),
                new Vector(battleScreen.getStageBounds().x - cloudTime * 4 + battleScreen.getStageBounds().width,
                        battleScreen.getStageBounds().y - 48
                                + (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                + (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.cos(cloudTime / 24.0) * 10 + 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
        battleScreen.renderObject(g, Images.getImage("blood_mist.png"),
                new Vector(battleScreen.getStageBounds().x - cloudTime * 4 + battleScreen.getStageBounds().width * 2,
                        battleScreen.getStageBounds().y - 48
                                + (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                + (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.cos(cloudTime / 24.0) * 10 + 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
        battleScreen.renderObject(g, Images.getImage("blood_mist.png"),
                new Vector(battleScreen.getStageBounds().x - cloudTime * 4 + battleScreen.getStageBounds().width * 3,
                        battleScreen.getStageBounds().y - 48
                                + (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0)
                                + (cloudTime > 720 ? ((720 - cloudTime) * (720 - cloudTime)) / 48 : 0) * 2
                                + Math.cos(cloudTime / 24.0) * 10 + 96),
                (int) battleScreen.getStageBounds().width, (int) battleScreen.getStageBounds().height, true, target);
    }

    public void renderHeart(Graphics g, ImageObserver target) {

        if (previousHeartPositions.size() >= 5) {
            previousHeartPositions.remove(0);
        }
        battleScreen.renderObject(g, Images.getImage("a_gods_heart.png"), heart.getPosition(), 620, 620, heartFrame,
                false, target);
    }

    @Override
    public void PRAISE() {
        System.out.println("PRAISE BIG SEAGULL");
    }
}