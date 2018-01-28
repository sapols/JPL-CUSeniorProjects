package mars.algorithm;

import mars.coordinate.Coordinate;
import mars.coordinate.DijkstraNode;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;
import java.util.*;

public class Dijkstra extends Algorithm {

    /*
     * Default constructor for an Dijkstra.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public Dijkstra(MarsRover r) {
        rover = r;
        map = rover.getMap();
    }

    public void findPath() {
        Set<DijkstraNode> nodeSet = new HashSet<DijkstraNode>();

        DijkstraNode goalNode = new DijkstraNode(rover.getEndPosition());

        DijkstraNode startNode = new DijkstraNode(rover.getStartPosition());
        startNode.setDistanceFromStart(0);

        // Add start node to nodeSet.
        nodeSet.add(startNode);

        // Add all neighbors to nodeSet.
        for (DijkstraNode n : startNode.getNeighbors()) {
            nodeSet.add(n);
        }

        while (!nodeSet.isEmpty()) {

            // Initialize min dist node.
            DijkstraNode minDistNode = new DijkstraNode(new Coordinate(-1, -1));

            // Get node with min dist from start from nodeSet.
            double min = Double.POSITIVE_INFINITY;
            for (DijkstraNode n : nodeSet) {
                if (n.getDistanceFromStart() < min) {
                    minDistNode = n;
                    min = n.getDistanceFromStart();
                }
            }

            // Remove min node from set.
            nodeSet.remove(minDistNode);

            for (DijkstraNode n : minDistNode.getNeighbors()) {
                // Only look at notes we have note visited.
                if (!minDistNode.hasVisited(n)) {
                    // Add to nodeSet.
                    nodeSet.add(n);
                    //  total distance to new node
                    double totalDistToNewNode = minDistNode.getDistanceFromStart() + minDistNode.distBetween(n);

                    // Shorter path has been found.
                    if (totalDistToNewNode < n.getDistanceFromStart()) {
                        n.setDistanceFromStart(totalDistToNewNode);
                        n.setParent(minDistNode);
                    }

                    minDistNode.addToVisitedSet(n);
                }
            }
            if (minDistNode.currentIsGoal(goalNode)) {
                minDistNode.constructPath();

                List<Coordinate> fullPath = minDistNode.getFullPath();
                Collections.reverse(fullPath);
                output = new TerminalOutput(fullPath);

                break;
            }

            System.out.println(minDistNode.getPosition().getX() + " " + minDistNode.getPosition().getY());
        }
    }
}
