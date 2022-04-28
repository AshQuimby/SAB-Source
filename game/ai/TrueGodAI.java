package game.ai;

import game.AssBall;
import game.Player;
import game.physics.Ledge;
import game.physics.Vector;
import game.projectile.Projectile;
import game.stage.Platform;
import game.physics.AABB;

public class TrueGodAI extends AI {

    boolean[] toBePressed = { false, false, false, false, false, false, false };

    public void press(int readableKeyID) {
        toBePressed[readableKeyID] = true;
    }

    public void tap(int readableKeyID) {
        if (!owner.readableKeys[readableKeyID])
            toBePressed[readableKeyID] = true;
    }

    public void release(int readableKeyID) {
        toBePressed[readableKeyID] = false;
    }

    private void releaseAll() {
        release(Player.UP);
        release(Player.DOWN);
        release(Player.LEFT);
        release(Player.RIGHT);
        release(Player.JUMP);
        release(Player.ATTACK);
        release(Player.PARRY);
    }

    @Override
    public void update(Player player) {
        Player target = targetPlayer(player.battleScreen.getPlayerList());
        boolean doNotAttack = false;

        AssBall ball = nearestAssBall(player.battleScreen.getAssBalls());
        if (ball != null) {
            target = ball.fakePlayer;
        }

        if (target == null)
            return;

        Platform nearestPlatformBelow = nearestPlatformBelow(player.battleScreen.getStage().getPlatforms());
        Projectile targetProjectile = nearestEnemyProjectile(player.battleScreen.getProjectiles());
        boolean hasTargetProjectile = targetProjectile != null;

        Vector targetPos = Vector.add(target.hitbox.getCenter(), target.velocity);

        Vector targetProjectilePos = null;
        if (hasTargetProjectile) {
            targetProjectilePos = targetProjectile.hitbox.getCenter();
        }

        Vector playerPos = player.hitbox.getCenter();

        releaseAll();

        if (hasTargetProjectile) {
            AABB simulatedPlayerHitbox = player.hitbox.copy();
            AABB simulatedProjectileHitbox = targetProjectile.hitbox.copy();

            boolean willCollide = false;
            int ticksToCollision = 0;

            for (int i = 0; i < 10; i++) {
                simulatedPlayerHitbox.translate(player.velocity);
                simulatedProjectileHitbox.translate(targetProjectile.velocity);

                if (simulatedPlayerHitbox.overlaps(simulatedProjectileHitbox)) {
                    willCollide = true;
                    ticksToCollision = i;
                    break;
                }
            }

            if (willCollide) {
                if (ticksToCollision <= 2) {
                    tap(Player.PARRY);
                } else {
                    if (targetProjectilePos.y > playerPos.y - player.hitbox.height && player.velocity.y >= 0) {
                        tap(Player.JUMP);
                    }
                    if (targetProjectilePos.x < playerPos.x) {
                        press(Player.RIGHT);
                    }
                    if (targetProjectilePos.x > playerPos.x) {
                        press(Player.LEFT);
                    }
                }
            }
        }

        if (player.falling && nearestPlatformBelow != null) {
            targetPos = new Vector(nearestPlatformBelow.getHitbox().getRandomPoint().x,
                    nearestPlatformBelow.getHitbox().y);
        }

        if (player.battleScreen.getStage().colliding(
                new AABB(player.hitbox.x + player.velocity.x * 20, player.hitbox.getY2(), player.hitbox.width, 1024))) {
            if (playerPos.x < targetPos.x - (player.hitbox.width + target.hitbox.width) / 2) {
                press(Player.RIGHT);
            } else if (playerPos.x > targetPos.x + (player.hitbox.width + target.hitbox.width) / 2) {
                press(Player.LEFT);
            } else {
                doNotAttack = true;
                if (player.direction == -1) {
                    press(Player.LEFT);
                } else {
                    press(Player.RIGHT);
                }
            }

            if (playerPos.y > targetPos.y + player.hitbox.height && player.velocity.y >= 0) {
                if (player.jumps > 0) {
                    tap(Player.JUMP);
                } else if (Math.abs(playerPos.x - targetPos.x) < player.hitbox.width + target.hitbox.width) {
                    press(Player.UP);
                    press(Player.ATTACK);
                }
            }
        } else {
            if (playerPos.x < targetPos.x) {
                press(Player.RIGHT);
            } else if (playerPos.x > targetPos.x) {
                press(Player.LEFT);
            }

            tap(Player.ATTACK);
        }

        if (!player.battleScreen.getStage()
                .colliding(new AABB(player.hitbox.x, player.hitbox.getY2(), player.hitbox.width, 1024))) {
            Ledge nearestLedge = nearestLedge(player.battleScreen.getStage().ledges);
            if (nearestLedge != null) {
                Vector ledgePos = nearestLedge.hitbox.getCenter();

                if (playerPos.x < ledgePos.x) {
                    release(Player.ATTACK);
                    release(Player.LEFT);
                    press(Player.RIGHT);
                } else if (playerPos.x > ledgePos.y) {
                    release(Player.ATTACK);
                    release(Player.RIGHT);
                    press(Player.LEFT);
                }

                if (player.velocity.y >= 5) {
                    if (player.jumps > 0) {
                        tap(Player.JUMP);
                        press(Player.UP);
                    } else {
                        press(Player.UP);
                        press(Player.ATTACK);
                    }
                }
            }
        }

        if (Math.abs(playerPos.x - targetPos.x) < player.hitbox.width + target.hitbox.width) {
            if (Math.abs(playerPos.y - targetPos.y) > player.hitbox.height + target.hitbox.height) {
                if (playerPos.y < targetPos.y + 64) {
                    press(Player.DOWN);
                }
            }
            tap(Player.ATTACK);
        }
        
        
        
        if (player.frozen > 0)
            tap(Player.PARRY);
        if (player.grabbingLedge) {
            press(Player.JUMP);
        }

        if (player.finalAss) {
            if (Vector.distanceBetween(playerPos, targetPos) < 128) {
                releaseAll();
                tap(Player.ATTACK);
            }
        }

        if (target.respawnTimer > 0) {
            toBePressed[Player.ATTACK] = false;
        }

        if (doNotAttack)
            toBePressed[Player.ATTACK] = false;

        for (int i = 0; i < player.readableKeys.length; i++) {
            if (toBePressed[i])
                player.simulateKeyPress(i);
            else if (!toBePressed[i] && player.readableKeys[i])
                player.simulateKeyRelease(i);
        }
    }
}