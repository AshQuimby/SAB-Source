package game.stage;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.ImageObserver;

import java.util.ArrayList;
import game.physics.Ledge;
import game.particle.AgedParticle;
import game.physics.AABB;

public class OurResort extends Stage {

    private boolean stormyMode;
    private Platform redRing;
    private Platform whiteRing;
    int flashTime;

    public OurResort() {
        stormyMode = false;
        name = "Our Sports Resort";
        musicFile = "last_location";
        musicCredit = "[Placeholder] Last Location -Beat Thorn";
        backgroundImage = "our_sports_resort_background.png";
        safeBlastZone = new AABB(0, 0, 1152, 704);
        unsafeBlastZone = new AABB(-128, -128, 1152 + 256, 704 + 256);
        platforms = new ArrayList<Platform>();
        ledges = new ArrayList<Ledge>();
        stageObjects = new ArrayList<CustomStageObject>();
        stageObjects.add(new DrawnObject(576 - 128, 512, 256, 704, "our_sports_platform_supports.png"));
        stageObjects.add(new DrawnObject(576 - 128, 512, 256, 704, "our_sports_platform_supports_solo.png"));
        redRing = new Platform(576 - 128, 512, 256, 24, false, "our_sports_platform.png");
        platforms.add(redRing);
        flashTime = 0;
    }

    @Override
    public void postUpdate() {
        flashTime--;
        if (backgroundImage.equals("our_sports_resort_background.png")
                && (battleScreen.getPlayers(0).lives == 1 || battleScreen.getPlayers(1).lives == 1)) {
            backgroundImage = "our_sports_resort_background_stormy.png";
            whiteRing = new Platform(576 - 192 / 2, 512, 192, 24, false, "our_sports_platform_white_ring.png");
            platforms.add(0, whiteRing);
            platforms.add(0, new Platform(576 - 132 / 2, 512, 132, 24, false, "our_sports_platform_blue_ring.png"));
            stormyMode = true;
            flashTime = 30;
        }
        if (stormyMode) {
            if (redRing.hitbox.y < 1000) {
                stageObjects.get(0).position.y += 4;
                redRing.hitbox.y += 4;
            } else {
                if (whiteRing.hitbox.y < 1000) {
                    whiteRing.hitbox.y += 4;
                }
            }

            battleScreen.addParticle(
                    new AgedParticle(1500 * Math.random() - (1500 - 1152), 32, 16, 16, 1, 8, 8, "rain.png", 100));
        }
    }

    @Override
    public void preRenderUI(Graphics g, ImageObserver target) {
        if (flashTime > 0) {
            g.setColor(new Color(255, 255, 255, (int) (255 * (flashTime / 30.0))));
            g.fillRect(0, 0, 1152, 704);
        }
    }
}