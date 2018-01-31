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

    public void findPath() throws Exception {
        Set<DijkstraNode> nodeSet = new HashSet<DijkstraNode>();

        DijkstraNode startNode = new DijkstraNode(rover.getStartPosition());
        DijkstraNode goalNode = new DijkstraNode(rover.getEndPosition());

        double heightOfMap = map.getHeight();
        double widthOfMap = map.getWidth();

        for (int y = 1; y <= heightOfMap; y++) {
            for (int x = 1; x <= widthOfMap; x++) {
                Coordinate tmpCoordinate = new Coordinate(x,y);
                DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                tmpNode.setParent(null);
                nodeSet.add(tmpNode);
            }
        }

        startNode.setDistanceFromStart(0);

        // Add start node to nodeSet.
        nodeSet.add(startNode);

        while (!nodeSet.isEmpty()) {

            // Initialize min dist node.
            DijkstraNode minDistNode = new DijkstraNode(new Coordinate(-1, -1));
            minDistNode.setDistanceFromStart(Double.POSITIVE_INFINITY);

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
