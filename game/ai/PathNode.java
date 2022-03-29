package game.ai;

import java.util.ArrayList;
import java.util.List;

import game.physics.Vector;

public class PathNode {
    private Vector position;
    private List<PathNode> connections;

    public PathNode(Vector position) {
        this.position = position;
        this.connections = new ArrayList<>();
    }

    public PathNode(Vector position, PathNode... nodes) {
        this.position = position;
        this.connections = new ArrayList<>();

        for (PathNode node : nodes) {
            connections.add(node);
        }
    }

    public Vector getPosition() {
        return position;
    }

    public List<PathNode> getConnections() {
        return connections;
    }

    public void addConnection(PathNode connection) {
        connections.add(connection);
    }
}