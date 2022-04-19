package game.stage;

import java.util.ArrayList;

import game.physics.Ledge;
import game.physics.Vector;
import game.physics.AABB;

public class COBS extends Stage {
    private Vector plat1Pos = new Vector(-140, 512);
    private  Vector plat2Pos = new Vector(-140 - 104 - 280, 512);
    private  Vector plat3Pos = new Vector(140 + 104, 512);
    public COBS() {
        // ℭ𝔥𝔲𝔯𝔠𝔥 𝔬𝔣 𝔅𝔦𝔤 𝔖𝔢𝔞𝔤𝔲𝔩𝔩
        name = "Church of Big Seagull";
        musicFile = "seagull_ultima";
        musicCredit = "Seagull Ultima -Beat Thorn";
        backgroundImage = "cobs_background.png";
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new Platform(576 + (int) plat1Pos.x, (int) plat1Pos.y, 280, 72, false, "ruined_platform_1.png"));
        ledges.add(new Ledge(576 + (int) plat1Pos.x - 12, (int) plat1Pos.y + 4, 16, 76, 1));
        ledges.add(new Ledge(576 + (int) plat1Pos.x - 4 + 280, (int) plat1Pos.y + 4, 16, 76, -1));
        platforms.add(new Platform(576 + (int) plat2Pos.x, (int) plat2Pos.y, 280, 72, false, "ruined_platform_2.png"));
        ledges.add(new Ledge(576 + (int) plat2Pos.x - 12, (int) plat2Pos.y + 4, 16, 76, 1));
        ledges.add(new Ledge(576 + (int) plat2Pos.x - 4 + 280, (int) plat2Pos.y + 4, 16, 76, -1));
        platforms.add(new Platform(576 + (int) plat3Pos.x, (int) plat3Pos.y, 280, 72, false, "ruined_platform_3.png"));
        ledges.add(new Ledge(576 + (int) plat3Pos.x - 12, (int) plat3Pos.y + 4, 16, 76, 1));
        ledges.add(new Ledge(576 + (int) plat3Pos.x - 4 + 280, (int) plat3Pos.y + 4, 16, 76, -1));
    }

    @Override
    public void postUpdate() {
        plat1Pos.y += Math.sin(battleScreen.getGameTick() / 4.0) * 4;
        platforms.get(0).hitbox.y = plat1Pos.y;
    }
}