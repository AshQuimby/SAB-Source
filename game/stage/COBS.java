package game.stage;

import java.util.ArrayList;

import game.physics.Ledge;
import game.physics.Vector;
import game.physics.AABB;
import game.PsuedoPlayer;
import game.projectile.Reticle;
import game.projectile.final_asses.GodEye;
import game.Player;

public class COBS extends Stage {
    private Vector plat1Pos = new Vector(-140, 512);
    private Vector plat2Pos = new Vector(-140 - 104 - 280, 512);
    private Vector plat3Pos = new Vector(140 + 104, 512);
    private PsuedoPlayer stagePlayer = new PsuedoPlayer(battleScreen, false);
    public COBS() {
        name = "Church of Big Seagull";
        musicFile = "seagull_ultima";
        musicCredit = "Seagull Ultima -Beat Thorn";
        backgroundImage = "cobs_background.png";
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new MovingPlatform(576 + (int) plat1Pos.x, (int) plat1Pos.y, 280, 72, false, "ruined_platform_1.png", this));
        ledges.add(new Ledge(576 + (int) plat1Pos.x - 12, (int) plat1Pos.y + 4, 16, 76, 1));
        ledges.add(new Ledge(576 + (int) plat1Pos.x - 4 + 280, (int) plat1Pos.y + 4, 16, 76, -1));
        platforms.add(new MovingPlatform(576 + (int) plat2Pos.x, (int) plat2Pos.y, 280, 72, false, "ruined_platform_2.png", this));
        ledges.add(new Ledge(576 + (int) plat2Pos.x - 12, (int) plat2Pos.y + 4, 16, 76, 1));
        ledges.add(new Ledge(576 + (int) plat2Pos.x - 4 + 280, (int) plat2Pos.y + 4, 16, 76, -1));
        platforms.add(new MovingPlatform(576 + (int) plat3Pos.x, (int) plat3Pos.y, 280, 72, false, "ruined_platform_3.png", this));
        ledges.add(new Ledge(576 + (int) plat3Pos.x - 12, (int) plat3Pos.y + 4, 16, 76, 1));
        ledges.add(new Ledge(576 + (int) plat3Pos.x - 4 + 280, (int) plat3Pos.y + 4, 16, 76, -1));
        fastMode = 0;
    }
      
    private int fastMode;
      
    @Override
    public void preUpdate() {
        
        if (battleScreen.getGameTick() % 200 == 0)
           battleScreen.addProjectileAtCenter(new Reticle(stagePlayer));
        
        if (battleScreen.getGameTick() % 1000 == 0) {
           fastMode = 30;
        }
        
        if (--fastMode > 0 && battleScreen.getGameTick() % 15 == 0) {
            Vector spawnPos = null;
            if (Math.random() > 0.5) {
               spawnPos = new Vector(Math.random() > 0.5 ? battleScreen.getStage().getUnsafeBlastZone().x : battleScreen.getStage().getUnsafeBlastZone().getX2(), Math.random() * battleScreen.getStage().getUnsafeBlastZone().height + battleScreen.getStage().getUnsafeBlastZone().y);
            } else {
               spawnPos = new Vector(Math.random() * battleScreen.getStage().getUnsafeBlastZone().width + battleScreen.getStage().getUnsafeBlastZone().x, Math.random() > 0.5 ? battleScreen.getStage().getUnsafeBlastZone().y : battleScreen.getStage().getUnsafeBlastZone().getY2());
            }
            Player target = Math.random() > 0.5 ? battleScreen.getPlayers(0) : battleScreen.getPlayers(1);
            battleScreen.addProjectileAtCenter(new GodEye(spawnPos.x, spawnPos.y, Vector.normalize(Vector.sub(target.center(), spawnPos)).mul(50).x, 
            Vector.normalize(Vector.sub(target.center(), spawnPos)).mul(50).y, 10, 10, stagePlayer.keyLayout, stagePlayer));
        }
        
        ((MovingPlatform) platforms.get(0)).velocity.y = (Math.cos(battleScreen.getGameTick() / 16.0 - 2)) / 2.0;
        ((MovingPlatform) platforms.get(1)).velocity.y = (Math.cos(battleScreen.getGameTick() / 16.0)) / 2.0;
        ((MovingPlatform) platforms.get(2)).velocity.y = (Math.cos(battleScreen.getGameTick() / 16.0 + 2)) / 2.0;

        
        plat1Pos = platforms.get(0).hitbox.getPosition();
        ledges.get(0).hitbox.y = plat1Pos.y + 4;
        ledges.get(1).hitbox.y = plat1Pos.y + 4;
        
        plat2Pos = platforms.get(1).hitbox.getPosition();
        ledges.get(2).hitbox.y = plat2Pos.y + 4;
        ledges.get(3).hitbox.y = plat2Pos.y + 4;
        
        plat3Pos = platforms.get(2).hitbox.getPosition();
        ledges.get(4).hitbox.y = plat3Pos.y + 4;
        ledges.get(5).hitbox.y = plat3Pos.y + 4;
    }
}