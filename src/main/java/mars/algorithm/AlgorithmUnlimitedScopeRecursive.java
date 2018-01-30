package mars.algorithm;

import mars.coordinate.Coordinate;
import mars.coordinate.AStarCoordinate;
import mars.rover.MarsRover;
import mars.out.TerminalOutput;
import mars.map.TerrainMap;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 * Uses an A* search.
 */
public class AlgorithmUnlimitedScopeRecursive extends Algorithm {

    ArrayList<AStarCoordinate> path = new ArrayList<AStarCoordinate>();
    Coordinate goal;

    /**
     * Default constructor for an AlgorithmUnlimitedScopeRecursive.
     * rover - the rover being input
     * map - the map being used
     * goal - the end position the rover needs to get to
     *
     * @param r Input rover to test with
     */
    public AlgorithmUnlimitedScopeRecursive(MarsRover r) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
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

    public ArrayList<Coordinate> getPath() {
        ArrayList<Coordinate> convertedPath = new ArrayList<Coordinate>();
        for (Iterator<AStarCoordinate> i = path.iterator(); i.hasNext();){ //foreach coordinate in list
            AStarCoordinate item = i.next();
            convertedPath.add(new Coordinate(item.getX(),item.getY()));
        }
        return convertedPath;
    }

    /**
     * Find a path from start to goal with A*. Then output it.
     * Throw an exception if a path cannot be found.
     *
     * @param coords The list of coordinates being considered by the algorithm.
     */
    public void AStarSearch(ArrayList<AStarCoordinate> coords) throws Exception {
        if (coords.isEmpty()) {
            throw new Exception("WARNING: A path to the goal could not be found.");
        }
        else {
            AStarCoordinate thisCoord = coords.get(0);
            path.add(thisCoord);
            if (thisCoord.equals(goal)) { //if we found the goal
                output = new TerminalOutput(path);
            }
            else {
                ArrayList<AStarCoordinate> neighbors = getReachableNeighbors(thisCoord);
                coords.addAll(neighbors);
                sortCoordinatesByCost(coords);
                coords.remove(thisCoord);
                AStarSearch(coords);
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
                if (!(i == x && j == y)) { //if this is not the given coordinate "coord"
                    try {
                        AStarCoordinate potentialNeighbor = new AStarCoordinate(i, j);
                        potentialNeighbor.setCostSoFar(costSoFar+1); //TODO: diagonals should technically add sqrt(2), not 1
                        //TODO: Address issues with MarsRover.canTraverse
                        if (rover.canTraverse(coord, potentialNeighbor)) { //if rover could visit this coordinate, add it
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

}
