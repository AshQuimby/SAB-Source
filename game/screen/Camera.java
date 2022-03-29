package game.screen;

import game.physics.Vector;

import java.io.Serializable;

import game.physics.AABB;

public class Camera implements Serializable {
    private Vector position;
    private Vector target;
    private int viewportWidth;
    private int viewportHeight;
    private float zoom;
    private float targetZoom;
    private boolean fixed;

    public Camera(Vector position, int viewportWidth, int viewportHeight) {
        this.position = position;
        target = new Vector(0, 0);
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        zoom = 1;
        targetZoom = 1;
        fixed = false;
    }

    public Camera(int viewportWidth, int viewportHeight) {
        this(new Vector(0, 0), viewportWidth, viewportHeight);
    }
    
    public Vector getCenter() {
      return Vector.add(position, new Vector(viewportWidth, viewportHeight).div(2));
    }
    
    public void update() {
        if (fixed) {
            return;
        }

        Vector toTarget = Vector.sub(target, position);
        position.add(toTarget.div(8));
        zoom += (targetZoom - zoom) * .1f;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void fix() {
        fixed = true;
    }

    public void unFix() {
        fixed = false;
    }

    public void fixAtPosition(Vector position) {
        this.position = position;
        fixed = true;
    }

    public void setTarget(Vector target) {
        this.target = target;
    }
    
    public Vector getTarget() {
        return target;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void setTargetZoom(float zoom) {
        this.targetZoom = zoom;
    }
    
    public float getTargetZoom() {
        return targetZoom;
    }
    
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector transform(Vector position) {
        Vector dimensions = new Vector(viewportWidth, viewportHeight);
        Vector translated = Vector.sub(position, this.position);
        return Vector.add(translated.mul(zoom), dimensions.div(2));
    }
    
    public Vector transform(Vector position, float localZoom) {
        Vector dimensions = new Vector(viewportWidth, viewportHeight);
        Vector translated = Vector.sub(position, this.position);
        return Vector.add(translated.mul(localZoom), dimensions.div(2));
    }

    public AABB getViewport() {
        return new AABB(position.x - (viewportWidth / 2) / zoom, position.y - (viewportHeight / 2)
                / zoom, viewportWidth / zoom, viewportHeight / zoom);
    }
}