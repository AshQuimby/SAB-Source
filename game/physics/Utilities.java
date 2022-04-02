package game.physics;

import game.character.Character;

public final class Utilities {
    public static int toTicks(double seconds) {
        return (int) (seconds * 20);
    }

    public static double toSeconds(int ticks) {
        return ticks / 20.0;
    }

    public static double quickKnockbackDirection(boolean facingRight, double degrees) {
        return facingRight ? Math.toRadians(degrees) : reflectAngleOverY(Math.toRadians(degrees));
    }

    public static double reflectAngleOverY(double radians) {
        Vector vector = new Vector(1, 0).rotateBy(radians);
        vector.x *= -1;
        return vector.rotationOf();
    }

    public static double reflectAngleOverx(double radians) {
        Vector vector = new Vector(1, 0).rotateBy(radians);
        vector.y *= -1;
        return vector.rotationOf();
    }

    public static boolean charactersMatch(Character target, Character type) {
        return target.getClass() == type.getClass();
    }
}