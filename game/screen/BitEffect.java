package game.screen;

import java.io.Serializable;

import game.physics.Vector;

public class BitEffect implements Serializable {

    Vector position;
    Vector velocity;

    public BitEffect(Vector position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
    }
}