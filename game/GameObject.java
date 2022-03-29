package game;

import java.io.Serializable;

import game.physics.Vector;

public abstract class GameObject implements Serializable {
   public Vector pos = new Vector(0.0, 0.0);
}