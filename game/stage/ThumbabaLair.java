package game.stage;

import java.util.ArrayList;
import game.physics.Ledge;
import game.physics.Vector;
import game.physics.AABB;

public class ThumbabaLair extends Stage {
    public ThumbabaLair() {
        name = "Thumbaba's Lair";
        musicFile = "thumbaba_lair";
        musicCredit = "Thumbaba's Lair -a_viper";
        backgroundImage = "thumbaba_lair_background.png";
        p2SpawnOffset = new Vector(48, 0);
        p2SpawnOffset = new Vector(32, 0);
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        platforms.add(new Platform(576 - 136 / 2 + 192, 352 + 40, 136, 32, false, "finger_platform.png"));
        ledges.add(new Ledge(576 - 136 / 2 + 192 - 12, 352 + 42, 16, 36, 1));
        ledges.add(new Ledge(576 - 136 / 2 + 192 + 136 - 4, 352 + 42, 16, 36, -1));
        platforms.add(new Platform(576 - 136 / 2 - 108, 352 + 96, 136, 32, false, "finger_platform.png"));
        ledges.add(new Ledge(576 - 136 / 2 - 108 - 12, 352 + 96, 16, 36, 1));
        ledges.add(new Ledge(576 - 136 / 2 - 108 + 136 - 4, 352 + 96, 16, 36, -1));
        platforms.add(new Platform(576 - 136 / 2 + 392, 352 + 76, 136, 32, false, "finger_platform.png"));
        ledges.add(new Ledge(576 - 136 / 2 + 392 - 12, 352 + 76, 16, 36, 1));
        ledges.add(new Ledge(576 - 136 / 2 + 392 + 136 - 4, 352 + 76, 16, 36, -1));
        platforms.add(new Platform(576 - 136 / 2 - 408, 352 + 124, 136, 32, false, "finger_platform.png"));
        ledges.add(new Ledge(576 - 136 / 2 - 408 - 12, 352 + 124, 16, 36, 1));
        ledges.add(new Ledge(576 - 136 / 2 - 408 + 136 - 4, 352 + 124, 16, 36, -1));
    }
}