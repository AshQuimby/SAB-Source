package game.stage;

import java.util.ArrayList;
import game.physics.Ledge;
import game.physics.AABB;

public class LastLocation extends Stage {
    public LastLocation() {
        name = "Last Location";
        musicFile = "last_location";
        musicCredit = "Last Location -Beat Thorn";
        backgroundImage = "background.png";
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new Platform(576 - 256, 512, 512, 56, false, "last_location.png"));
        ledges.add(new Ledge(576 - 256 - 12, 516, 16, 56, 1));
        ledges.add(new Ledge(576 + 256 - 4, 516, 16, 56, -1));
    }
}