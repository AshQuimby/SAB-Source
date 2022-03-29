package game.stage;

import java.util.ArrayList;
import game.physics.Ledge;
import game.physics.AABB;
import game.physics.Vector;

public class Desert extends Stage {
    public Desert() {
        name = "Desert Bridge";
        musicFile = "desert_bridge";
        musicCredit = "Desert Bridge -AshQuimby";
        backgroundImage = "desert_background.png";
        maxZoomOut = 0.8f;
        p2SpawnOffset = new Vector(0, -128);
        p2SpawnOffset = new Vector(0, -128);
        safeBlastZone = new AABB(-72 - 36 * 2, -144, 1440 - 72 + 36 * 2, 704 * 1.25);
        unsafeBlastZone = new AABB(-144, -128 - 144, 1440, 704 * 1.25 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new Platform(576 - 720, 512 - 64, 1440, 256, false, "desert.png"));
    }
}