package game.stage;

public abstract class UpdatingPlatform extends Platform {

    public UpdatingPlatform(int x, int y, int width, int height, boolean canJumpThrough, String image) {
        super(x, y, width, height, canJumpThrough, image);
    }

    public abstract void update();

    @Override
    public boolean updates() {
        return true;
    }
}