package game.stage;

import java.io.Serializable;

import game.physics.Vector;
import game.physics.AABB;

public abstract class CustomStageObject implements Serializable {
    public Vector position;
    public int width;
    public int height;
    protected AABB hitbox;
    boolean hasHitbox;
    String image;

    public CustomStageObject(int x, int y, int width, int height, boolean hasHitbox, String image) {
        position = new Vector(x, y);
        this.width = width;
        this.height = height;
        if (hasHitbox) {
            hitbox = new AABB(x, y, width, height);
        }
        this.hasHitbox = hasHitbox;
        this.image = image;
    }

    public AABB getHitbox() {
        if (hasHitbox)
            return hitbox;
        System.out.println("Object " + this + " does not have a hitbox");
        return null;
    }

    public abstract void update();
}