package game.stage;

import java.util.ArrayList;
import game.physics.Ledge;
import game.physics.AABB;

public class OurResort extends Stage {
    public OurResort() {
        name = "Our Sports Resort";
        musicFile = "last_location";
        musicCredit = "Last Location -Beat Thorn";
        backgroundImage = "our_sports_resort_background.png";
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        stageObjects = new ArrayList<CustomStageObject>();
        stageObjects.add(new DrawnObject(576 - 128, 512, 256, 704, "our_sports_platform_supports.png"));
        platforms.add(new Platform(576 - 128, 512, 256, 24, false, "our_sports_platform.png"));
    }
}