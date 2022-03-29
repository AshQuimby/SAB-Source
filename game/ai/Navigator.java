package game.ai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.awt.Graphics;

import game.physics.AABB;
import game.physics.Vector;
import game.physics.CollisionLine;
import game.stage.Block;
import game.stage.Platform;
import game.stage.Stage;

public class Navigator implements Serializable {
    private Stage stage;
    private List<PathNode> nodes;

    public Navigator(Stage stage) {
        this.stage = stage;
        generateGraph();
    }

    private PathNode addNode(Vector position) {
        PathNode node = new PathNode(position);
        nodes.add(node);

        return node;
    }

    private void processPlatform(Platform platform) {
        if (platform.getClass() != Block.class) {
            AABB hitbox = platform.getHitbox();

            // The -1 Y offset prevents two nodes from the same platform from being
            // obstructed from each other.
            addNode(new Vector(hitbox.x - 32, hitbox.y - 1));
            addNode(new Vector(hitbox.getX2() + 32, hitbox.y - 1));
        }
    }

    private void connectNode(PathNode node) {
        for (PathNode other : nodes) {
            if (node == other)
                continue;

            Vector position = node.getPosition();
            Vector otherPosition = other.getPosition();

            double distanceToOther = Vector.distanceBetween(position, otherPosition);
            if (distanceToOther > 512)
                continue;

            double angleToOther = Math.atan2(otherPosition.y - position.y, otherPosition.x - position.x);
            CollisionLine ray = new CollisionLine(position.x, position.y, distanceToOther, angleToOther);
            boolean obstructed = ray.collidingWithHitboxes(stage.getNonJumpThroughHitboxes());

            if (!obstructed) {
                node.addConnection(other);
            }

        }
    }

    private void generateGraph() {
        nodes = new ArrayList<>();

        for (Platform platform : stage.getPlatforms()) {
            processPlatform(platform);
        }

        // for (Player player : stage.battleScreen.getPlayerList()) {
        // addNode(player.center().clone());
        // }
    }

    public void render(Graphics g) {
        for (PathNode node : nodes) {
            g.drawRect((int) node.getPosition().x - 5, (int) node.getPosition().y - 5 + 48, 10, 10);

            for (PathNode connection : node.getConnections()) {
                g.drawLine((int) node.getPosition().x, (int) node.getPosition().y + 48,
                        (int) connection.getPosition().x,
                        (int) connection.getPosition().y + 48);
            }
        }
    }

    public List<Vector> getPath(Vector a, Vector b) {
        Map<PathNode, Integer> values = new HashMap<>();
        Queue<PathNode> open = new LinkedList<>();
        Set<PathNode> closed = new HashSet<>();

        PathNode start = addNode(a);
        PathNode dest = addNode(b);

        for (PathNode node : nodes) {
            connectNode(node);
        }

        open.add(dest);
        closed.add(dest);
        values.put(dest, 0);

        while (open.size() > 0) {
            PathNode current = open.poll();

            for (PathNode node : current.getConnections()) {
                if (!closed.contains(node)) {
                    open.add(node);
                    closed.add(node);
                    values.put(node, values.get(current) + 1);

                    if (node == start)
                        break;
                }
            }
        }

        if (!closed.contains(start))
            return null;

        List<Vector> path = new ArrayList<>();
        PathNode currentNode = start;
        path.add(currentNode.getPosition());

        while (currentNode != dest) {
            PathNode next = null;
            for (PathNode neighbor : currentNode.getConnections()) {
                if (next == null || values.get(neighbor) < values.get(next)) {
                    next = neighbor;
                }
            }

            currentNode = next;
            path.add(currentNode.getPosition());
        }

        return path;
    }
}