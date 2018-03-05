package mars.algorithm.unlimited;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.coordinate.AStarCoordinate;
import mars.out.MapImageOutput;
import mars.out.OutputFactory;
import mars.rover.MarsRover;
import mars.out.TerminalOutput;
import mars.map.TerrainMap;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 * Uses an A* search.
 */
public class UnlimitedAStarRecursive extends Algorithm {

    ArrayList<AStarCoordinate> visitedCoords = new ArrayList<AStarCoordinate>();
    Coordinate goal;
    AStarCoordinate targetCoord;

    /**
     * Default constructor for an UnlimitedAStarRecursive.
     * rover - the rover being input
     * map - the map being used
     * goal - the end position the rover needs to get to
     *
     * @param r The rover
     * @param output The output type specified during this algorithm's instantiation
     */
    public UnlimitedAStarRecursive(MarsRover r, String output) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        outputClass = output;
        targetCoord = new AStarCoordinate(rover.getStartPosition());
    }

    /**
     * Second constructor for an UnlimitedAStarRecursive which defaults output to "TerminalOutput".
     * rover - the rover being input
     * map - the map being used
     * goal - the end position the rover needs to get to
     *
     * @param r The rover
     */
    public UnlimitedAStarRecursive(MarsRover r) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        outputClass = "TerminalOutput";
        targetCoord = new AStarCoordinate(rover.getStartPosition());
    }

    /**
     * Method which starts this search algorithm.
     */
    public void findPath() throws Exception {
        ArrayList<AStarCoordinate> start = new ArrayList<AStarCoordinate>();
        start.add(new AStarCoordinate(rover.getStartPosition()));

        try {
            AStarSearch(start);
        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<AStarCoordinate> getPath() {
        return constructPath(targetCoord);
    }

    /**
     * Find a path from start to goal with A*. Then output it.
     * Throw an exception if a path cannot be found.
     *
     * @param unvisitedCoords The list of coordinates being considered by the algorithm.
     */
    public void AStarSearch(ArrayList<AStarCoordinate> unvisitedCoords) throws Exception {
        if (unvisitedCoords.isEmpty()) {
            throw new Exception("WARNING: A path to the goal could not be found.");
        }
        else {
            AStarCoordinate thisCoord = unvisitedCoords.get(0);
            visitedCoords.add(thisCoord);
            if (thisCoord.equals(goal)) { //if we found the goal
                targetCoord = thisCoord; //for getPath to reference

                //TODO: remove this commented-out code
                //Generates Output based on the type specified during this algorithm's instantiation
                //OutputFactory.getOutput(this);
            }
            else {
                ArrayList<AStarCoordinate> unvisitedNeighbors = getReachableUnvisitedNeighbors(thisCoord);
                for (AStarCoordinate n : unvisitedNeighbors) {
                    if (!coordIsInSet(n, unvisitedCoords)) //Don't add duplicates
                        unvisitedCoords.add(n);
                }
                sortCoordinatesByCost(unvisitedCoords);
                unvisitedCoords.remove(thisCoord);
                AStarSearch(unvisitedCoords);
            }
        }
    }

    //----Helper methods-----------------------------------------------------------------------------------------------

    /**
     * Given a coordinate, return all the neighboring coordinates
     * which can be visited by this algorithm's rover (meaning that
     * the slope between the coordinates is not too steep).
     * Possible neighbors are all eight coordinates surrounding the given one.
     * Do not consider already-visited coordinates.
     * @param coord The coordinate whose neighbors will be found.
     */
    public ArrayList<AStarCoordinate> getReachableUnvisitedNeighbors(AStarCoordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        double costSoFar = coord.getCostSoFar();
        ArrayList<AStarCoordinate> neighbors = new ArrayList<AStarCoordinate>();

        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                if (!(i == x && j == y)) { //if this is not the given coordinate "coord". TODO: probably not needed now because of visited set logic
                    try {
                        AStarCoordinate potentialNeighbor = new AStarCoordinate(i, j);
                        potentialNeighbor.setCostSoFar(costSoFar+1); //TODO: diagonals should technically add sqrt(2), not 1
                        potentialNeighbor.setParent(coord);
                        //TODO: Address issues with MarsRover.canTraverse (are we using Robert's idea?)
                        if (rover.canTraverse(coord, potentialNeighbor)
                                && !coordHasBeenVisited(potentialNeighbor)) { //if rover could visit this coordinate and hasn't already, add it
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

        return Math.sqrt((Math.pow((x2-x1),2) + Math.pow((y2-y1),2)));
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
     * Check if there is a node matching ours in the visited set
     * @param coord Coordinate to test for
     * @return Boolean whether coord has been visited
     */
    private boolean coordHasBeenVisited(AStarCoordinate coord) {
        return coordIsInSet(coord, visitedCoords); //TODO: if this change causes merge conflict, nix it.
    }

    /**
     * Constructs a path for A* by traversing nodes' parents.
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
