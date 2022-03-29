package game.stage;

import java.util.ArrayList;
import game.physics.Ledge;
import game.physics.AABB;

public class Warzone extends Stage {
    public Warzone() {
        name = "Warzone";
        musicFile = "battle_placeholder";
        musicCredit = "[PLACEHOLDER] Space Boss Chiptune -AshQuimby";
        backgroundImage = "warzone_background.png";
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new Platform(576 - 256, 512, 512, 56, false, "warzone.png"));
        // Side platforms
        platforms.add(new Platform(576 - 226, 400, 128, 24, true, "warzone_platform.png"));
        platforms.add(new Platform(576 + 98, 400, 128, 24, true, "warzone_platform.png"));
        // Top platform
        platforms.add(new Platform(576 - 64, 300, 128, 24, true, "warzone_platform.png"));
        ledges.add(new Ledge(576 - 256 - 12, 516, 16, 56, 1));
        ledges.add(new Ledge(576 + 256 - 4, 516, 16, 56, -1));
    }
}