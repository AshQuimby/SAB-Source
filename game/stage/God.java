package game.stage;

public class God extends COBS {
    public God() {
        super();
        name = "God";
        stageObjects.add(new MarvinBox(576 - 64, 0, 128, 128, false, "marvin_box.png", this));
    }
}