package mods.Frost_Elevator;

import java.util.ArrayList;
import java.util.Random;
import game.physics.Ledge;
import modloader.ModStage;
import game.stage.Platform;
import game.physics.AABB;

public class Mountains extends ModStage {
    public Mountains() {
        name = "Mountains";
        musicFile = "Lost_in_the_Frost";
        musicCredit = "Lost in the Frost -Beat Thorn";
        backgroundImage = "mountains_background.png";
        if (new Random().nextInt(20) == 1) {
           backgroundImage = "mountains_background_unique.png";
        }
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new Platform(176, 604, 800, 200, false, "mountains.png"));
        platforms.add(new Platform(476, 304, 200, 100, false, "mountains_2.png"));
        ledges.add(new Ledge(976, 624, 16, 16, -1));
        ledges.add(new Ledge(160, 624, 16, 16, 1));
        ledges.add(new Ledge(676, 304, 16, 16, -1));
        ledges.add(new Ledge(460, 304, 16, 16, 1));
    }
}