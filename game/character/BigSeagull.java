package game.character;

import game.Player;
import game.physics.Vector;
import game.SoundEngine;
import game.projectile.*;
import game.projectile.final_asses.GodSeagullLeg;
import game.projectile.final_asses.TrueGod;

public class BigSeagull extends Character {
    public BigSeagull() {
        width = 96;
        height = 96;
        hitboxWidth = 64;
        hitboxHeight = 76;
        jumps = 5;
        doubleJumpDropoff = .36;
        weight = 48;
        speed = 1.8;
        drag = 0.9;
        jumpHeight = 44;
        offset = new Vector(10, 20);
        fileName = "big_seagull.png";
        walkFrameTimer = 4;
        characterName = "Big Seagull";
        overrideWalkAnimation = true;
        altCount = 2;
        description = new String[] { "Big Seagull is an ancient god of hunger and destruction.",
                "\nBig Seagull watches all from high above, only descending to",
                "\ncollect offerings given by the mortals below the clouds.",
                "\nBig Seagull is one of the only True Gods.",
                "\n",
                "\nDebut: Real Life" };
    }

    boolean uniqueAttackAnimation = false;

    @Override
    public void neutralAttack(Player player) {
        player.battleScreen.addProjectileAtCenter(
                new WindBlast(player.center().x, player.center().y - 28, player.direction,
                        Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140), player.keyLayout,
                        player));
        SoundEngine.playSound("gust");
        player.frame = 4;
        player.endLag = 12;

        uniqueAttackAnimation = true;
    }

    @Override
    public void sideAttack(Player player) {
        player.battleScreen.addProjectile(new Peck(player.hitbox.x, -1000, player.direction, 0, 10,
                Math.toRadians(200) + ((player.direction + 1) / 2) * Math.toRadians(140),
                player.keyLayout, player.direction, player));

        player.frame = 4;
        player.endLag = 8;
        uniqueAttackAnimation = false;
    }

    @Override
    public void upAttack(Player player) {
        player.battleScreen.addProjectile(
                new Glide(player.hitbox.x, player.hitbox.y, player.keyLayout, player));
        SoundEngine.playSound("gust");
        player.frame = 4;
        player.velocity.y = -28;
        player.touchingStage = false;
        player.falling = true;
    }

    @Override
    public void downAttack(Player player) {
        player.battleScreen.addProjectileAtCenter(
                new FeatherDart(player.center().x, player.center().y, 14 * player.direction, -4, player.direction,
                        Math.toRadians(220) + ((player.direction + 1) / 2) * Math.toRadians(120), player.keyLayout,
                        player));
        player.battleScreen.addProjectileAtCenter(
                new FeatherDart(player.center().x, player.center().y, 14 * player.direction, 4, player.direction,
                        Math.toRadians(140) + ((player.direction + 1) / 2) * Math.toRadians(240), player.keyLayout,
                        player));
        player.battleScreen.addProjectileAtCenter(
                new FeatherDart(player.center().x, player.center().y, 18 * player.direction, 0, player.direction,
                        Math.toRadians(180) + ((player.direction + 1) / 2) * Math.toRadians(180), player.keyLayout,
                        player));
        SoundEngine.playSound("gust");
        player.frame = 4;
        player.endLag = 12;

        uniqueAttackAnimation = true;
    }

    @Override
    public void chargeAttack(Player player, int charge) {
    }

    @Override
    public void finalAss(Player player) {
        player.battleScreen.addProjectile(new TrueGod(player));
    }

    @Override
    public void uniqueUpdatePostFreezeCases(Player player) {
        // Feather falling
        if (player.velocity.y > 0) {
            player.velocity.y -= 1;
        }
        if (player.velocity.y > 0 && player.falling) {
            player.velocity.y -= 0.5;
        }
    }

    @Override
    public void uniqueAnimations(Player player) {
        if (player.touchingStage) {
            if (player.endLag <= 0) {
                if (player.readableKeys[Player.LEFT] || player.readableKeys[Player.RIGHT]) {
                    if (++player.walkTimer >= player.selectedChar.walkFrameTimer) {
                        if (++player.frame >= 6) {
                            player.frame = 1;
                        }
                        player.walkTimer = 0;
                    }
                }
                if (!player.readableKeys[Player.LEFT] && !player.readableKeys[Player.RIGHT]
                        || player.readableKeys[Player.LEFT] && player.readableKeys[Player.RIGHT]) {
                    player.frame = 0;
                }
            }

        } else {
            if (player.frame < 7) {
                player.frame = 7;
            }
            if (!player.falling) {
                if (++player.walkTimer >= walkFrameTimer) {
                    if (++player.frame >= 11) {
                        player.frame = 7;
                    }
                    player.walkTimer = 0;
                }
            } else {
                if (++player.walkTimer >= walkFrameTimer * 2) {
                    if (++player.frame >= 11) {
                        player.frame = 7;
                    }
                    player.walkTimer = 0;
                }
            }
        }
        if (player.endLag > 0) {
            if (!uniqueAttackAnimation) {
                if (player.endLag < 4) {
                    player.frame = 12;
                } else {
                    player.frame = 11;
                }
            } else {
                if (player.endLag < 7) {
                    player.frame = 5;
                } else {
                    player.frame = 13;
                }
            }
        }
        if (player.grabbingLedge) {
            player.frame = 14;
        }
        if (player.knockBack.len() > 2) {
            player.frame = 13;
        }
        if (player.respawnTimer > 0) {
            player.frame = 0;
        }
        if (player.parryTimer > 0) {
            player.frame = 5;
        }
    }
}