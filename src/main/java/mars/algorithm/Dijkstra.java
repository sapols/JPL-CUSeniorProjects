package mars.algorithm;

import mars.coordinate.Coordinate;
import mars.coordinate.DijkstraNode;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;

import javax.sound.midi.SysexMessage;
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

        int goalX = goalNode.getPosition().getX();
        int goalY = goalNode.getPosition().getY();


        // Instead of loading entire map, let's try only loading the square from start to goal.
        // For example, if start is 10, 10 and goal is 20, 20, the rectangle will have:
        // x vals from 10 - 20, and y vals from 10 - 20.
        for (int y = 1; y <= goalY; y++) {
            for (int x = 1; x <= goalX; x++) {
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

        double currentMin = Double.POSITIVE_INFINITY;
        for (DijkstraNode n : nodeSet) {
            System.out.println(n.getPosition().getX());
            if (n.getDistanceFromStart() < currentMin) {
                currentMin = n.getDistanceFromStart();
            }
        }
        System.out.println(currentMin);

        /*
        while (!nodeSet.isEmpty()) {

            double currentMin = Double.POSITIVE_INFINITY;
            for (DijkstraNode n : nodeSet) {
                if (n.getDistanceFromStart() < currentMin) {
                    currentMin = n.getDistanceFromStart();
                }
            }


        }
        */

    }
}
