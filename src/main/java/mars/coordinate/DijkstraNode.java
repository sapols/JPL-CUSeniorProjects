package mars.coordinate;
import java.util.*;

public class DijkstraNode {
    private List<Coordinate> fullPath = new ArrayList<Coordinate>();
    private Coordinate position;
    private DijkstraNode parent = null;
    private double distanceFromStart = Double.POSITIVE_INFINITY;
    private Set<DijkstraNode> visitedSet;

    /**
     * DijkstraNode constructor.
     * Sets position and creates empty visitedSet.
     * @param _position position
     */
    public DijkstraNode(Coordinate _position) {
        position = _position;
        visitedSet = new HashSet<DijkstraNode>();
    }

    public Coordinate getPosition() {
        return position;
    }
    public void setPosition(Coordinate _position) {
        position = _position;
    }
    public DijkstraNode getParent() {
        return parent;
    }
    public void setParent(DijkstraNode _parent) {
        parent = _parent;
    }
    public double getDistanceFromStart() {
        return distanceFromStart;
    }
    public void setDistanceFromStart(double _dist) {
        distanceFromStart = _dist;
    }
    public void addToVisitedSet(DijkstraNode n) {
        visitedSet.add(n);
    }
    public List<Coordinate> getFullPath() {
        return fullPath;
    }

    /**
     * Finds neighbors of a given node
     * @param n neighbor node
     * @return boolean. true if we've already visited the node
     */
    public boolean hasVisited(DijkstraNode n) {
        Coordinate position = n.position;
        int xPosition = position.getX();
        int yPosition = position.getY();

        // Iterate over visited set for current node.
        for (DijkstraNode node : visitedSet) {
            if ((node.position.getX() == xPosition) && (node.position.getY() == yPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds neighbors of a given node
     * @return list of nodes neighboring the current node
     */
    public List<DijkstraNode> getNeighbors() {
        // TODO
        // Check if on edge of map - breaks if we're on the pixel right next
        // to the edge.
        List<DijkstraNode> neighborNodeList = new ArrayList<DijkstraNode>();

        Coordinate currentCoordinate = this.getPosition();
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

        DijkstraNode nodeLeft = new DijkstraNode(coordinateLeft);
        DijkstraNode nodeUpLeft = new DijkstraNode(coordinateUpLeft);
        DijkstraNode nodeUp = new DijkstraNode(coordinateUp);
        DijkstraNode nodeUpRight = new DijkstraNode(coordinateUpRight);
        DijkstraNode nodeRight = new DijkstraNode(coordinateRight);
        DijkstraNode nodeDownRight = new DijkstraNode(coordinateDownRight);
        DijkstraNode nodeDown = new DijkstraNode(coordinateDown);
        DijkstraNode nodeDownLeft = new DijkstraNode(coordinateDownLeft);

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
     * Determines distance between two nodes
     * @param neighborNode neighbor node to the current node
     * @return distance between current node and neighbor node
     */
    public double distBetween(DijkstraNode neighborNode) {
        int currentX = this.getPosition().getX();
        int currentY = this.getPosition().getY();
        int neighborX = neighborNode.getPosition().getX();
        int neighborY = neighborNode.getPosition().getY();

        return Math.sqrt(Math.pow(currentX - neighborX, 2) + Math.pow(currentY - neighborY, 2));
    }

    /**
     * Constructs a path for A* by traversing nodes' parents.
     */
    public List<Coordinate> constructPath() {
        DijkstraNode currentNode = this;
        while (currentNode != null) {
            fullPath.add(currentNode.getPosition());
            currentNode = currentNode.getParent();
        }
        return fullPath;
    }

    /**
     * Check if the two nodes are the same
     * @param goalNode second node to check
     * @return boolean if the nodes are the same
     */
    public boolean currentIsGoal(DijkstraNode goalNode) {
        double currentX = this.getPosition().getX();
        double currentY = this.getPosition().getY();
        double goalX = goalNode.getPosition().getX();
        double goalY = goalNode.getPosition().getY();

        if ((currentX == goalX) && (currentY == goalY)) {return true;}
        else {return false;}
    }
}