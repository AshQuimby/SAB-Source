package game.character;

import game.Images;
import game.Player;
import game.SoundEngine;
import game.animation.Animation;
import game.particle.AnimatedParticle;
import game.physics.*;
import game.projectile.*;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class EmptySoldier extends Character {
    private Animation animation;
    private Animation idle;
    private Animation run;

    private Animation screwAttack;
    private Animation angrySoulAttack;

    public int spirit;
    private int spiritCharger;
    private boolean superCharged;
    private int superChargeTime;

    private interface VoidFunction {
        void apply();
    }

    public EmptySoldier() {
        width = 64;
        height = 68;
        hitboxWidth = 40;
        hitboxHeight = 64;
        superCharged = false;
        superChargeTime = 0;
        jumps = 1;
        doubleJumpDropoff = 1;
        weight = 43;
        speed = 1.3;
        spirit = 100;
        spiritCharger = 0;
        drag = 0.943;
        jumpHeight = 33;
        offset = new Vector(12, 0);
        overrideAttackAnimation = true;
        fileName = "empty_soldier.png";
        altCount = 0;
        chargingParticle = "smoke.png";
        walkFrameTimer = 2;
        characterName = "Empty Soldier";
        description = new String[] { "Hailing from the burrows of an ancient underground kingdom,",
                "Empty Soldier's only purpose is to purge the land of the infestation that killed its people.",
                "Don't let its small stature fool you however, as its sharp screw and deadly soul blasts",
                "can make quick work of any challenger.",
                "",
                "Debut: Empty Soldier" };

        idle = new Animation(1, true);
        idle.addFrames(0);

        run = new Animation(3, true);
        run.addFrames(1, 2, 3);

        screwAttack = new Animation(5, false);
        screwAttack.addFrames(4, 5, 0);

        angrySoulAttack = new Animation(3, true);
        angrySoulAttack.addFrames(10, 11, 12);

        animation = idle;
    }

    private void runMethodWithSpiritCost(VoidFunction f, int spiritCost) {
        if (spirit >= spiritCost) {
            spirit -= spiritCost;
            f.apply();
        }
    }

    @Override
    public void neutralAttack(Player player) {
        screwAttack.reset();
        animation = screwAttack;
        player.endLag = 9;
        player.battleScreen
                .addProjectileAtCenter(new Screw(player.center().x, player.center().y, 0, 0,
                        1, player.direction == 1 ? -Math.toRadians(20) : -Math.toRadians(160),
                        player.keyLayout, player.direction, player));
    }

    @Override
    public void sideAttack(Player player) {
        player.endLag = 4;

        runMethodWithSpiritCost(() -> {
            player.battleScreen
                    .addProjectileAtCenter(new AngrySoul(player.center().x, player.center().y, player.direction * 23, 0,
                            Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
                            player.keyLayout, player.direction, player));
            player.frame = 4;
            player.endLag = 14;
            player.velocity.y = 0;

            angrySoulAttack.reset();
            animation = angrySoulAttack;
        }, 15);
    }

    @Override
    public void upAttack(Player player) {
        boolean canSpawn = true;
        for (Projectile projectile : player.battleScreen.getProjectiles()) {
            if (projectile.getClass() == ViceroyWings.class && projectile.ownerPlayer == player) {
                canSpawn = false;
            }
        }
        if (canSpawn)
            runMethodWithSpiritCost(() -> {
                player.battleScreen
                        .addProjectileAtCenter(new ViceroyWings(player.center().x, player.center().y, 0, -30,
                                Math.toRadians(270),
                                player.keyLayout, player.direction, player));
                player.touchingStage = false;
                player.velocity.y = -jumpHeight;
            }, 30);
    }

    @Override
    public void downAttack(Player player) {
        if (player.touchingStage)
            return;

        runMethodWithSpiritCost(() -> {
            player.battleScreen.addProjectileAtCenter(
                    new ShadowPlunge(player.center().x, player.center().y, 38, 0, player.keyLayout,
                            player.direction,
                            player));
            player.endLag = 23;
        }, 25);
    }

    @Override
    public void finalAss(Player player) {
        superCharged = true;
        superChargeTime = 300;
        SoundEngine.playSound("spirit_charge");

        for (int i = 0; i < 100; i++) {
            player.battleScreen.addParticle(new AnimatedParticle(
                    player.center().x,
                    player.center().y,
                    (Math.random() - .5) * 16,
                    (Math.random() - .5) * 16,
                    1,
                    32,
                    32,
                    Math.random() > 0.5 ? "shadowling_particle.png" : "shadowling_particle_flipped.png",
                    6,
                    7));
        }
    }

    @Override
    public void chargeAttack(Player player, int charge) {
    }

    @Override
    public void uniqueUpdatePreEverything(Player player) {
        if (superCharged) {
            player.endLag -= 2;
            spirit += 2;
            speed = 1.8;
            jumpHeight = 45;
            fileName = "empty_soldier_shade.png";

            superChargeTime--;
            if (superChargeTime <= 0) {
                superCharged = false;
            }
        } else {
            jumpHeight = 33;
            speed = 1.3;
            fileName = "empty_soldier";

            if (player.costume > 0) {
                fileName += "_alt_" + player.costume;
            }

            fileName += ".png";
        }

        if (++spiritCharger >= 4 && spirit < 100) {
            spirit++;
            spiritCharger = 0;
        }

        if (spirit > 100) {
            spirit = 100;
        }

        if (animation == angrySoulAttack) {
            player.velocity.x *= .9;
            player.velocity.y *= .3;
            player.velocity.y -= 1;
        }
    }

    @Override
    public void uniqueOnHit(Player player, game.MutableBoolean ignoreDamage, Player playerSource, int damage,
            double knockback) {
    }

    @Override
    public void uniqueAnimations(Player player) {
        player.frame = animation.update();

        if (animation == idle) {
            if (player.readableKeys[Player.LEFT] ^ player.readableKeys[Player.RIGHT]) {
                animation = run;
            }
        } else if (animation == run) {
            if (!(player.readableKeys[Player.LEFT] ^ player.readableKeys[Player.RIGHT])) {
                animation = idle;
            }
        } else if (animation == screwAttack) {
            if (animation.isDone()) {
                animation = idle;
            }
        } else if (animation == angrySoulAttack) {
            if (player.endLag <= 5) {
                animation = idle;
            }
        }

        if (animation == idle || animation == run) {
            if (player.velocity.y < 0) {
                player.frame = 6;
            } else if (player.velocity.y > 0 && !player.touchingStage) {
                player.frame = 7;
            }

            if (player.tookDamage)
                player.frame = 9;

            if (player.grabbingLedge) {
                player.frame = 8;
            }

            if (player.endLag > 0) {
                player.frame = 0;
            }
        }
    }

    @Override
    public void preRender(Player player, Graphics g, ImageObserver target) {
        if (superCharged) {
            player.battleScreen.renderObject(g, Images.getImage("ball_sack.png"),
                    new Vector(player.pos.x - 32, player.pos.y - 32),
                    width + 64,
                    height + 64, player.direction < 0, target);

            for (int i = 0; i < 4; i++) {
                Vector offset = new Vector(0, 0);
                offset.add(new Vector((Math.random() - 0.5) * 12, (Math.random() - 0.5) * 12));
                player.battleScreen.renderObject(g, Images.getImage(fileName),
                        Vector.add(player.pos, offset), width,
                        height,
                        player.frame, player.direction < 0, target);
            }
        }
    }

    @Override
    public void renderUIElements(Player player, Graphics g, ImageObserver target) {
        g.drawImage(Images.getImage("spirit_bar_back.png"), 512 + (128 * player.keyLayout * 2), 600,
                target);
        g.drawImage(Images.getImage(superCharged ? "spirit_bar_charged.png" : "spirit_bar.png"),
                512 + (128 * player.keyLayout * 2) + 8,
                (int) (600 + 56 * ((100.0 - spirit) / 100)) + 8,
                512 + (128 * player.keyLayout * 2 + 56) + 8, 600 + 56 + 8, 0,
                (int) (0 + 56 * ((100.0 - spirit) / 100)),
                56, 56, target);
    }
}