package game.ai;

public enum Neighbor {
    NORTH(0, 1),
    NORTHEAST(1, 1),
    EAST(1, 0),
    SOUTHEAST(1, -1),
    SOUTH(0, -1),
    SOUTHWEST(-1, -1),
    WEST(-1, 0),
    NORTHWEST(-1, 1);

    public final int dx;
    public final int dy;

    public static final Neighbor[] cardinalDirections = new Neighbor[] {
            NORTH, SOUTH, EAST, WEST
    };

    Neighbor(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}