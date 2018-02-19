package mars.algorithm.unlimited;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.coordinate.DijkstraNode;
import mars.out.MapImageOutput;
import mars.out.OutputFactory;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;
import java.util.*;

public class Dijkstra extends Algorithm {

    ArrayList<Coordinate> fullPath = new ArrayList<Coordinate>();

    /**
     * Default constructor for an Dijkstra.
     *
     * @param r The rover
     * @param output The output type specified during this algorithm's instantiation
     */
    public Dijkstra(MarsRover r, String output) {
        rover = r;
        map = rover.getMap();
        outputClass = output;
    }
    
    public ArrayList<Coordinate> getPath() {
        return fullPath;
    }

    public void findPath(){
        Vector<DijkstraNode> nodeVector = new Vector<DijkstraNode>();

        DijkstraNode startNode = new DijkstraNode(rover.getStartPosition());
        DijkstraNode goalNode = new DijkstraNode(rover.getEndPosition());

        int startX = startNode.getPosition().getX();
        int startY = startNode.getPosition().getY();

        int goalX = goalNode.getPosition().getX();
        int goalY = goalNode.getPosition().getY();


        // Instead of loading entire map, let's try only loading the square from start to goal with a 100% buffer
        // For example, if start is 10, 10 and goal is 20, 20, the rectangle will have:
        // x vals from 10 - 20, and y vals from 10 - 20.
        int yRange = Math.abs(goalY - startY);
        int xRange = Math.abs(goalX - startX);
        int halfYRange = yRange / 2;
        int halfXRange = xRange / 2;
        int bufferStartY;
        int bufferStartX;
        int bufferGoalY;
        int bufferGoalX;
        if (startX < goalX) {
            bufferStartX = startX - halfXRange;
            bufferGoalX = goalX + halfXRange;
            if (startY < goalY) {
                bufferStartY = startY - halfYRange;
                bufferGoalY = goalY + halfYRange;
                for (int y = bufferStartY; y <= bufferGoalY; y++) {
                    for (int x = bufferStartX; x <= bufferGoalX; x++) {
                        Coordinate tmpCoordinate = new Coordinate(x, y);
                        DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                        tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                        tmpNode.setParent(null);
                        nodeVector.add(tmpNode);
                    }
                }
            }
            else { // startY > goalY
                bufferStartY = startY + halfYRange;
                bufferGoalY = goalY - halfYRange;
                for (int y = bufferGoalY; y <= bufferStartY; y++) {
                    for (int x = bufferStartX; x <= bufferGoalX; x++) {
                        Coordinate tmpCoordinate = new Coordinate(x, y);
                        DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                        tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                        tmpNode.setParent(null);
                        nodeVector.add(tmpNode);
                    }
                }
            }
        }
        else { // startX > goalX
            System.out.println("2");
            bufferStartX = startX + halfXRange;
            bufferGoalX = goalX - halfXRange;
            if (startY < goalY) {
                System.out.println("3");
                bufferStartY = startY - halfYRange;
                bufferGoalY = goalY + halfYRange;
                for (int y = bufferStartY; y <= bufferGoalY; y++) {
                    for (int x = bufferGoalX; x <= bufferStartX; x++) {
                        Coordinate tmpCoordinate = new Coordinate(x, y);
                        DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                        tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                        tmpNode.setParent(null);
                        nodeVector.add(tmpNode);
                    }
                }
            }
            else { // startY > goalY
                bufferStartY = startY + halfYRange;
                bufferGoalY = goalY - halfYRange;
                for (int y = bufferGoalY; y <= bufferStartY; y++) {
                    for (int x = bufferGoalX; x <= bufferStartX; x++) {
                        Coordinate tmpCoordinate = new Coordinate(x, y);
                        DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                        tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                        tmpNode.setParent(null);
                        nodeVector.add(tmpNode);
                        System.out.println("1");
                    }
                }
            }
        }

        for (int i = 0; i < nodeVector.size(); i++) {
            if (nodeVector.get(i).getPosition().getX() == startX && nodeVector.get(i).getPosition().getY() == startY) {
                nodeVector.get(i).setDistanceFromStart(0);
            }
        }

        while (!nodeVector.isEmpty()) {
            boolean goalFound = false;

            DijkstraNode minNode = getClosestNode(nodeVector);

            if (minNode.getPosition().getX() == Integer.MAX_VALUE) {
                // No path found?
                break;
            }

            removeNodeFromVector(nodeVector, minNode);

            List<DijkstraNode> neighborList = minNode.getNeighbors();
            for (int i = 0; i < neighborList.size(); i++) {

                DijkstraNode currentNode = neighborList.get(i);

                int currentX = currentNode.getPosition().getX();
                int currentY = currentNode.getPosition().getY();

                // Checks to see if current neighbor is in the vector.
                boolean inVector = false;
                for (DijkstraNode nVec : nodeVector) {
                    if (neighborList.get(i).getPosition().getX() == nVec.getPosition().getX() &&
                            neighborList.get(i).getPosition().getY() == nVec.getPosition().getY()) {
                        inVector = true;
                    }
                }
                if (inVector) {
                    //System.out.println("hi");
                    // If current neighbor is in the vector, we're going to get the new distance for that node,
                    // check to see if it's traversable, and if so, set distance and parent for the node.
                    double totalDist = minNode.getDistanceFromStart() + minNode.distBetween(neighborList.get(i));
                    if (totalDist < neighborList.get(i).getDistanceFromStart()) {

                        for (int q = 0; q < nodeVector.size(); q++) {
                            if (nodeVector.get(q).getPosition().getX() == currentX &&
                                    nodeVector.get(q).getPosition().getY() == currentY) {
                                // Alter node within vector.
                                if (rover.canTraverse(minNode.getPosition(), currentNode.getPosition())) {
                                    nodeVector.get(q).setDistanceFromStart(totalDist);
                                    nodeVector.get(q).setParent(minNode);
                                }
                            }
                        }
                    }
                }
                if (neighborList.get(i).currentIsGoal(goalNode)) {
                    // Goal node has been found.

                    for (int q = 0; q < nodeVector.size(); q++) {
                        if (nodeVector.get(q).getPosition().getX() == neighborList.get(i).getPosition().getX() &&
                                nodeVector.get(q).getPosition().getY() == neighborList.get(i).getPosition().getY()) {
                            // Alter node within vector.
                            List<Coordinate> tmpList = nodeVector.get(q).constructPath();
                            fullPath = new ArrayList<Coordinate>(tmpList);
                            goalFound = true;
                        }
                    }

                }
            }
            if (goalFound) {
                break;
            }
        }
        Collections.reverse(fullPath);

    }

    private void removeNodeFromVector(Vector<DijkstraNode> nodeVector, DijkstraNode minNode) {
        int minNodeXPos = minNode.getPosition().getX();
        int minNodeYPos = minNode.getPosition().getY();

        for (int i = 0; i < nodeVector.size(); i++) {
            DijkstraNode n = nodeVector.get(i);
            if ((n.getPosition().getX() == minNodeXPos) && (n.getPosition().getY() == minNodeYPos)) {
                nodeVector.remove(i);
            }
        }
    }

    private DijkstraNode getClosestNode(Vector<DijkstraNode> nodeVector) {
        double currentMin = Double.POSITIVE_INFINITY;
        DijkstraNode tmp = new DijkstraNode(new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE));
        for (int i = 0; i < nodeVector.size(); i++) {
            DijkstraNode n = nodeVector.get(i);
            if (n.getDistanceFromStart() < currentMin) {
                currentMin = n.getDistanceFromStart();
                tmp = n;
            }
        }
        return tmp;
    }


}
