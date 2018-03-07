package mars.algorithm.unlimited;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.out.MapImageOutput;
import mars.out.OutputFactory;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 */
public class UnlimitedAStarNonRecursive extends Algorithm {

    ArrayList<Coordinate> fullPath = new ArrayList<Coordinate>();

    /**
     * Default constructor for an UnlimitedAStarNonRecursive.
     *
     * @param r The rover
     * @param output The output type specified during this algorithm's instantiation
     */
    public UnlimitedAStarNonRecursive(MarsRover r, String output) {
        rover = r;
        map = rover.getMap();
        outputClass = output;
    }

    /**
     * Second constructor for an UnlimitedAStarNonRecursive which defaults output to "TerminalOutput".
     *
     * @param r The rover
     */
    public UnlimitedAStarNonRecursive(MarsRover r) {
        rover = r;
        map = rover.getMap();
        outputClass = "TerminalOutput";
    }

    public ArrayList<Coordinate> getPath() {
        return fullPath;
    }

    /**
     * Implementation of A*
     */
    public void findPath() throws Exception {
        // Main method to find path.
        boolean success = false;

        Coordinate startPosition = rover.getStartPosition();
        Coordinate endPosition= rover.getEndPosition();

        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();

        Node startNode = new Node(startPosition);
        Node goalNode = new Node(endPosition);

        startNode.setGScore(0);
        startNode.setFScore(estimateHeuristic(startNode, goalNode));
        startNode.setParent(null);

        openList.add(startNode);

        // While the open set isn't empty...
        while (!openList.isEmpty()) {
            // Get the node with the lowest F score.
            Node currentNode = getLowestFScore(openList);

            if (currentIsGoal(currentNode, goalNode)) {
                constructPath(currentNode);
                success = true;
                break;
            }

            openList.remove(currentNode);
            closedList.add(currentNode);

            // Get the list of neighbor nodes.
            List<Node> neighborList = getNeighbors(currentNode);
            // For each neighbor node...
            for (Node neighbor: neighborList) {
                double tentativeGScore = Double.POSITIVE_INFINITY;
                // Ignore neighbors if it's too steep and we can't go there.
                if (rover.canTraverse(currentNode.getPosition(), neighbor.getPosition())) {

                    // Ignore neighbors if we've already evaluated them.
                    if (isNodeInList(neighbor, closedList)) {
                        continue;
                    }
                    // If we're at an undiscovered node...
                    if (!isNodeInList(neighbor, openList)) {
                        openList.add(neighbor);
                    }

                    // Gets tentative G score.
                    tentativeGScore = currentNode.getGScore() + distBetween(currentNode, neighbor);

                    // If the tentative G score is higher than the neighbor's G score, this is
                    // not a better path.
                    if (tentativeGScore >= neighbor.getGScore()) {
                        continue;
                    }
                }
                else {
                    closedList.add(currentNode);
                }

                // If we get to this point, the path is optimal up to this point.
                // So, we record the current Node for the full path.
                neighbor.setParent(currentNode);
                neighbor.setGScore(tentativeGScore);
                neighbor.setFScore(neighbor.getGScore() + estimateHeuristic(neighbor, goalNode));
            }
        }

        if(success) {
            Collections.reverse(fullPath);
        }else{
            throw new Exception("WARNING: A path to the goal could not be found.");
        }

    }

    /**
     * Check if there is a node matching ours in a list
     * @param node node to test for
     * @param list list to check
     * @return boolean whether node is in list
     */
    private boolean isNodeInList(Node node, List<Node> list) {
        int x = node.getPosition().getX();
        int y = node.getPosition().getY();
        for (Node n : list) {
            int tmpX = n.getPosition().getX();
            int tmpY = n.getPosition().getY();
            if ((x == tmpX) && (y == tmpY)) {return true;}
        }
        return false;
    }

    /**
     * Constructs a path for A* by traversing nodes' parents.
     * @param currentNode node to start traversing
     */
    private void constructPath(Node currentNode) {
        while (currentNode != null) {
            fullPath.add(currentNode.getPosition());
            currentNode = currentNode.getParent();
        }
    }

    /**
     * Determines distance between two nodes
     * @param currentNode first node to check
     * @param neighborNode neighbor node to the current node
     * @return distance between current node and neighbor node
     */
    private double distBetween(Node currentNode, Node neighborNode) {
        int currentX = currentNode.getPosition().getX();
        int currentY = currentNode.getPosition().getY();
        int neighborX = neighborNode.getPosition().getX();
        int neighborY = neighborNode.getPosition().getY();

        return Math.sqrt(Math.pow(currentX - neighborX, 2) + Math.pow(currentY - neighborY, 2));
    }

    /**
     * Finds neighbors of a given node
     * @param currentNode node to check
     * @return list of nodes neighboring the current node
     */
    private List<Node> getNeighbors(Node currentNode) {
        // TODO
        // Check if on edge of map - breaks if we're on the pixel right next
        // to the edge.
        List<Node> neighborNodeList = new ArrayList<Node>();

        Coordinate currentCoordinate = currentNode.getPosition();
        int currentX = currentCoordinate.getX();
        int currentY = currentCoordinate.getY();

        // since we're on a grid, treat our graph as such and determine neighbors like that
        Coordinate coordinateLeft = new Coordinate(currentX - 1, currentY);
        Coordinate coordinateUpLeft = new Coordinate(currentX - 1, currentY + 1);
        Coordinate coordinateUp = new Coordinate(currentX, currentY + 1);
        Coordinate coordinateUpRight = new Coordinate(currentX + 1, currentY + 1);
        Coordinate coordinateRight = new Coordinate(currentX + 1, currentY);
        Coordinate coordinateDownRight = new Coordinate(currentX + 1, currentY - 1);
        Coordinate coordinateDown = new Coordinate(currentX, currentY - 1);
        Coordinate coordinateDownLeft = new Coordinate(currentX - 1, currentY - 1);

        Node nodeLeft = new Node(coordinateLeft);
        Node nodeUpLeft = new Node(coordinateUpLeft);
        Node nodeUp = new Node(coordinateUp);
        Node nodeUpRight = new Node(coordinateUpRight);
        Node nodeRight = new Node(coordinateRight);
        Node nodeDownRight = new Node(coordinateDownRight);
        Node nodeDown = new Node(coordinateDown);
        Node nodeDownLeft = new Node(coordinateDownLeft);

        neighborNodeList.add(nodeLeft);
        neighborNodeList.add(nodeUpLeft);
        neighborNodeList.add(nodeUp);
        neighborNodeList.add(nodeUpRight);
        neighborNodeList.add(nodeRight);
        neighborNodeList.add(nodeDownRight);
        neighborNodeList.add(nodeDown);
        neighborNodeList.add(nodeDownLeft);

        return neighborNodeList;
    }

    /**
     * A* function to get lowest F Score from a list of nodes
     * @param list list of nodes to check
     * @return node with lowest F Score
     */
    private Node getLowestFScore(List<Node> list) {
        Node lowestNode = list.get(0);
        for (Node n : list) {
            if (n.getFScore() < lowestNode.getFScore()) {
                lowestNode = n;
            }
        }
        return lowestNode;
    }

    /**
     * Heuristic function for A* implementing Manhattan distance between two nodes
     * @param currentNode current node
     * @param goalNode goal node
     * @return Manhattan distance
     */
    private double estimateHeuristic(Node currentNode, Node goalNode) {
        // Manhattan estimate. Using only horizontal/vertical distances as
        // opposed to "how to crow flies".

        Coordinate currentPosition = currentNode.getPosition();
        double currentXPos = currentPosition.getX();
        double currentYPos = currentPosition.getY();

        Coordinate goalPosition = goalNode.getPosition();
        double goalXPos = goalPosition.getX();
        double goalYPos = goalPosition.getY();

        return Math.abs(currentXPos - goalXPos) + Math.abs(currentYPos - goalYPos);
    }

    /**
     * Check if the two nodes are the same
     * @param currentNode first node to check
     * @param goalNode second node to check
     * @return boolean if the nodes are the same
     */
    private boolean currentIsGoal(Node currentNode, Node goalNode) {
        double currentX = currentNode.getPosition().getX();
        double currentY = currentNode.getPosition().getY();
        double goalX = goalNode.getPosition().getX();
        double goalY = goalNode.getPosition().getY();

        if ((currentX == goalX) && (currentY == goalY)) {return true;}
        else {return false;}
    }

    public class Node {
        private Coordinate position;
        private Node parent;
        private double gScore = Double.POSITIVE_INFINITY;
        private double fScore = Double.POSITIVE_INFINITY;

        public Node(Coordinate _position) {
            position = _position;
        }
        public Coordinate getPosition() {
            return position;
        }
        public void setPosition(Coordinate _position) {
            position = _position;
        }
        public Node getParent() {
            return parent;
        }
        public void setParent(Node _parent) {
            parent = _parent;
        }
        public double getFScore() {
            return fScore;
        }
        public void setFScore(double _fScore) {
            fScore = _fScore;
        }
        public double getGScore() {
            return gScore;
        }
        public void setGScore(double _gScore) {
            gScore = _gScore;
        }
    }

}
