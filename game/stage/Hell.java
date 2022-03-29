package game.stage;

import java.util.ArrayList;
import game.physics.Ledge;
import game.physics.AABB;
import game.physics.Vector;

public class Hell extends Stage {
    public Hell() {
        name = "Hell";
        musicFile = "genetically_engineered_bad";
        musicCredit = "[Hell on the ears] Genetically Engineered Bad -AshQuimby";
        backgroundImage = "background.png";
        maxZoomOut = 0.45f;
        p1SpawnOffset = new Vector(-1028, -512);
        p2SpawnOffset = new Vector(0, -128);
        safeBlastZone = new AABB(0 - 640, -320, 1152 + 1280, 704 + 480);
        unsafeBlastZone = new AABB(-128 - 640, -128 - 320, 1152 + 256 + 1280, 704 + 256 + 480);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new Platform(576, 640, 770, 128, false, "icon.png"));
        platforms.add(new Platform(2139, 48, 770, 256, false, "server_icon.png"));
        platforms.add(new Platform(-810, -23, 770, 322, false, "client_icon.png"));
        platforms.add(new Platform(-258, 324, 400, 400, false, "icon.png"));
        platforms.add(new Wobbler(520, 520, 64, 64, false, "barrel.png", this));
        stageObjects.add(new MarvinBox(1000, -256, 128, 128, false, "marvin_box.png", this));
        int i = 4;
        for (i = 0; 14 + 2 > i; i -= 2) {
            i += 5;
            platforms.add(new Platform(-420 + 32 * i, 69, 8 * i, 8, true, "p" + (i % 2 + 1) + "arrow.png"));
            platforms.add(new Platform(-200 + -420 / 2 + 32 * 4 * i, 69 + 16 * i, 32 * 4, 32, true,
                    "p" + (i % 2 + 1) + "spawn_platform.png"));
        }
    }
}