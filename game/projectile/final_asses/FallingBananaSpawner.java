package game.projectile.final_asses;

import game.Images;
import game.Player;
import game.SoundEngine;
import game.physics.*;
import game.projectile.Projectile;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class FallingBananaSpawner extends Projectile {
    public FallingBananaSpawner(Player ownerPlayer) {
        life = 400;
        alive = true;
        this.owner = ownerPlayer.playerId;
        this.ownerPlayer = ownerPlayer;
        fileName = "none.png";
        unreflectable = true;
        unParryable = true;
        hitbox = new AABB(ownerPlayer.hitbox.x, ownerPlayer.hitbox.y, width, height);
        pos = hitbox.getPosition();
        cloudTime = 0;
    }

    int cloudTime;

    @Override
    public void update() {
        if (life > 100) {
            if (life % 20 == 0) {
                SoundEngine.playSound("swish");;
                battleScreen.addProjectileAtCenter(new FallingBanana((Math.random() * battleScreen.getStage().getSafeBlastZone().width) - battleScreen.getStage().getSafeBlastZone().x, battleScreen.getStage().getSafeBlastZone().y - 80, 0, 0, 10, Math.toRadians(270), owner, ownerPlayer));
            }
        }
        hitbox.setCenter(ownerPlayer.center());
        cloudTime++;
        if (--life == 0) {
            alive = false;
        }
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        battleScreen.renderObject(g, Images.getImage("banana_cloud_background.png"), new Vector(battleScreen.getStage().getSafeBlastZone().x + cloudTime / 3, battleScreen.getStage().getSafeBlastZone().y - 48 - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0) - (cloudTime > 256 ? ((256 - cloudTime) * (256 - cloudTime)) / 48 : 0) * 2 + Math.cos(cloudTime / 24.0) * 10 - 10), (int) battleScreen.getStage().getSafeBlastZone().width, (int) battleScreen.getStage().getSafeBlastZone().height, true, target);
        battleScreen.renderObject(g, Images.getImage("banana_cloud_background.png"), new Vector(battleScreen.getStage().getSafeBlastZone().x + cloudTime / 3 - battleScreen.getStage().getSafeBlastZone().width, battleScreen.getStage().getSafeBlastZone().y - 48 - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0) - (cloudTime > 256 ? ((256 - cloudTime) * (256 - cloudTime)) / 48 : 0) * 2 + Math.cos(cloudTime / 24.0) * 10 - 10), (int) battleScreen.getStage().getSafeBlastZone().width, (int) battleScreen.getStage().getSafeBlastZone().height, true, target);
    }

    @Override
    public void lateRender(Graphics g, ImageObserver target) {
        battleScreen.renderObject(g, Images.getImage("banana_cloud.png"), new Vector(battleScreen.getStage().getSafeBlastZone().x + cloudTime, battleScreen.getStage().getSafeBlastZone().y - 48 - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0) - (cloudTime > 256 ? ((256 - cloudTime) * (256 - cloudTime)) / 48 : 0) * 2 + Math.sin(cloudTime / 24.0) * 10 - 10), (int) battleScreen.getStage().getSafeBlastZone().width, (int) battleScreen.getStage().getSafeBlastZone().height, true, target);
        battleScreen.renderObject(g, Images.getImage("banana_cloud.png"), new Vector(battleScreen.getStage().getSafeBlastZone().x + cloudTime - battleScreen.getStage().getSafeBlastZone().width, battleScreen.getStage().getSafeBlastZone().y - 48 - (cloudTime < 128 ? ((128 - cloudTime) * (128 - cloudTime)) / 48 : 0) - (cloudTime > 256 ? ((256 - cloudTime) * (256 - cloudTime)) / 48 : 0) * 2 + Math.sin(cloudTime / 24.0) * 10 - 10), (int) battleScreen.getStage().getSafeBlastZone().width, (int) battleScreen.getStage().getSafeBlastZone().height, true, target);
    }

    @Override
    public void kill() {
    }
}
