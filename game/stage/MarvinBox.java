package game.stage;

import game.Player;
import game.character.Marvin;
import game.physics.AABB;
import game.ai.*;

public class MarvinBox extends CustomStageObject {

    Stage stage;
    int life;

    public MarvinBox(int x, int y, int width, int height, boolean canJumpThrough, String image, Stage stage) {
        super(x, y, width, height, false, image);
        this.stage = stage;
        life = 0;
    }

    @Override
    public void update() {
        if (life % 60 == 0) {
            game.character.Character boxSpawn;
            boxSpawn = new Marvin();
            Player player = new Player(-1, boxSpawn, stage.battleScreen, 1, 0, new GodAI());
            AABB rect = new AABB(position.x, position.y, width, height);
            player.hitbox.setCenter(rect.getCenter());
            stage.battleScreen.getPlayerList().add(player);
        }
        life++;
    }
}