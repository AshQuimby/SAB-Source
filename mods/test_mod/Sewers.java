package mods.test_mod;

import java.util.ArrayList;
import game.physics.Ledge;
import modloader.ModStage;
import game.stage.Platform;
import game.physics.AABB;

public class Sewers extends ModStage {
    public Sewers() {
        name = "Sewers";
        musicFile = "battle_placeholder";
        musicCredit = "[PLACEHOLDER] Space Boss Chiptune -AshQuimby";
        backgroundImage = "sewers_background.png";
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new Platform(576 - 210, 512, 420, 128, false, "sewers.png"));
        platforms.add(new Platform(0, 540, 256, 92, false, "sewers_secondary.png"));
        platforms.add(new Platform(1152 - 256, 540, 256, 92, false, "sewers_secondary.png"));
        ledges.add(new Ledge(576 - 210 - 12, 516, 16, 16, 1));
        ledges.add(new Ledge(576 + 210 - 4, 516, 16, 16, -1));
        ledges.add(new Ledge(0 - 12, 544, 16, 16, 1));
        ledges.add(new Ledge(0 + 256 - 4, 544, 16, 16, -1));
        ledges.add(new Ledge(1152 - 256 - 12, 544, 16, 16, 1));
        ledges.add(new Ledge(1152 - 4, 544, 16, 16, -1));
    }
}