package mars.algorithm.unlimited;

import mars.algorithm.Algorithm;
import mars.coordinate.AStarCoordinate;
import mars.coordinate.Coordinate;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.sqrt;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 * Uses an IDA* search algorithm.
 */
public class UnlimitedIDAStar extends Algorithm {

    ArrayList<AStarCoordinate> fullPath = new ArrayList<AStarCoordinate>();
    Coordinate goal;

    /**
     * Default constructor for an UnlimitedIDAStar.
     *
     * @param r The rover
     * @param output The output type specified during this algorithm's instantiation
     */
    public UnlimitedIDAStar(MarsRover r, String output) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        outputClass = output;
    }

    /**
     * Second constructor for an UnlimitedIDAStar which defaults output to "TerminalOutput".
     *
     * @param r The rover
     */
    public UnlimitedIDAStar(MarsRover r) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        outputClass = "TerminalOutput";
    }

    /**
     * @return fullPath the path constructed by findPath method
     */
    public ArrayList<? extends Coordinate> getPath() { return fullPath; }

    /**
     * Method which starts this search algorithm.
     */
    public void findPath() throws Exception {
        try {
            IDAStarSearch(new AStarCoordinate(rover.getStartPosition()));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Find a path from start to goal with IDA*. Then output it.
     * Throw an exception if a path cannot be found.
     *
     * @param thisNode The coordinate being considered by the algorithm.
     */
    public void IDAStarSearch(AStarCoordinate thisNode) throws Exception {
        if (coordIsInSet(new AStarCoordinate(goal), fullPath)) { return; }
        if (thisNode == null) {
            throw new Exception("WARNING: A path to the goal could not be found.");
        }
        else {
            if (thisNode.equals(goal)) { //if we found the goal
                fullPath = constructPath(thisNode);
            }
            else {
                ArrayList<AStarCoordinate> neighbors = getReachableNeighbors(thisNode);
                sortCoordinatesByCost(neighbors);
                for (AStarCoordinate n : neighbors) {
                    IDAStarSearch(n);
                }
            }
        }
    }

    //----Helper methods-----------------------------------------------------------------------------------------------

    /**
     * Given a coordinate, return all the neighboring coordinates
     * which can be visited by this algorithm's rover (meaning that
     * the slope between the coordinates is not too steep).
     * Possible neighbors are all eight coordinates surrounding the given one.
     * @param coord The coordinate whose neighbors will be found.
     */
    public ArrayList<AStarCoordinate> getReachableNeighbors(AStarCoordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        double costSoFar = coord.getCostSoFar();
        ArrayList<AStarCoordinate> neighbors = new ArrayList<AStarCoordinate>();

        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                if (!(i == x && j == y)) { //if this is not the given coordinate "coord".
                    try {
                        AStarCoordinate potentialNeighbor = new AStarCoordinate(i, j);
                        if (i == x || j == y) {
                            potentialNeighbor.setCostSoFar(costSoFar+1);
                        } else {
                            potentialNeighbor.setCostSoFar(costSoFar+sqrt(2));
                        }
                        potentialNeighbor.setParent(coord);
                        if (rover.canTraverse(coord, potentialNeighbor)) { //if rover could visit this coordinate
                            neighbors.add(potentialNeighbor);
                        }
                    } catch (Exception e) {
                        //"potentialNeighbor" was out of bounds of the map, so no-op.
                    }
                }
            }
        }

        return neighbors;
    }

    /**
     * Given a set of coordinates, sort them according to their overall cost,
     * which is the euclidean distance to the rover's goal coordinate
     * plus the distance traveled from the start to a coordinate.
     */
    public void sortCoordinatesByCost(ArrayList<AStarCoordinate> coords) {
        for (AStarCoordinate c : coords) {
            c.setDistanceToGoal(getDistanceToGoal(c));
        }

        Collections.sort(coords); //Do the sort, per the "compareTo" method in AStarCoordinate
    }

    /**
     * Given a coordinate, calculate its euclidean distance to the rover's goal coordinate
     * (using the distance formula derived from the Pythagorean theorem).
     */
    public double getDistanceToGoal(AStarCoordinate coord) {
        int x1 = coord.getX();
        int y1 = coord.getY();
        int x2 = goal.getX();
        int y2 = goal.getY();

        return sqrt((Math.pow((x2-x1),2) + Math.pow((y2-y1),2)));
    }

    /**
     * Check if there is a node matching ours in the given set
     * @param coord Coordinate to test for
     * @return Boolean whether coord has been visited
     */
    private boolean coordIsInSet(AStarCoordinate coord, ArrayList<AStarCoordinate> set) {
        for (AStarCoordinate c : set) {
            if (c.equals(coord))
                return true;
        }
        return false;
    }

    /**
     * Constructs a path for IDA* by traversing nodes' parents.
     * @param coord Node to start traversing
     */
    private ArrayList<AStarCoordinate> constructPath(AStarCoordinate coord) {
        ArrayList<AStarCoordinate> path = new ArrayList<AStarCoordinate>();
        while (coord != null) {
            path.add(coord);
            coord = coord.getParent();
        }

        Collections.reverse(path);
        return path;
    }

}
