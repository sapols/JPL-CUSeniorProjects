package mars.algorithm;

import mars.coordinate.Coordinate;
import mars.rover.MarsRover;
import mars.out.TerminalOutput;
import mars.map.TerrainMap;
import java.util.Collections;

import java.util.ArrayList;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 */
public class AlgorithmUnlimitedScope extends Algorithm {

    ArrayList<Coordinate> path = new ArrayList<Coordinate>();

    /*
     * Default constructor for an AlgorithmUnlimitedScope.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public AlgorithmUnlimitedScope(MarsRover r) throws Exception {
        rover = r;
        map = r.getMap();

        path.add(rover.getStartPosition());
        ArrayList<Coordinate> neighbors = getReachableNeighbors(rover.getStartPosition());
        try {
            findPath(neighbors);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void findPath(ArrayList<Coordinate> coords) throws Exception {
        //find a path with A*
        //TODO: This is technically "best first search". Consider cost to arrive at points to make it "A*".
        if (coords.isEmpty()) {
            throw new Exception("WARNING: A path to the goal could not be found.");
        }
        else {
            Coordinate thisCoord = coords.get(0);
            path.add(thisCoord);
            if (thisCoord.compareTo(rover.getEndPosition()) == 0) { //if we found the goal. TODO: check this for correctness
                output = new TerminalOutput(path);
            }
            else {
                ArrayList<Coordinate> children = getReachableNeighbors(thisCoord);
                children.remove(thisCoord); //TODO: check this for correctness
                findPath(children);
            }
        }
    }

    //----Helper methods-----------------------------------------------------------------------------------------------

    /*
     * Given a coordinate, return all the neighboring coordinates
     * which can be visited by this algorithm's rover (meaning that
     * the slope between the coordinates is not too steep).
     * Possible neighbors are all eight coordinates surrounding the given one.
     */
    public ArrayList<Coordinate> getReachableNeighbors(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();

        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                if (!(i == x && j == y)) { //if this is not the given coordinate "coord"
                    try {
                        Coordinate potentialNeighbor = new Coordinate(i, j);
                        double slope = rover.getSlope(coord, potentialNeighbor);

                        if (slope <= rover.getMaxSlope()) { //if rover could visit this coordinate, add it
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

    /*
     * Given a set of coordinates, sort them according to their
     * euclidean distance to the rover's goal coordinate.
     */
    public void sortNeighborsByDistanceToGoal(Coordinate[] neighbors) {
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

        //set the distance to the rover's goal for each coordinate and add to List
        for (Coordinate c : neighbors) {
            c.setDistanceToGoal(getDistanceToGoal(c));
            coordinates.add(c);
        }

        Collections.sort(coordinates); //Do the sort, per the "compareTo" method in Coordinate

        //Copy the contents of the sorted "coordinates" back into "neighbors"
        for (int i = 0; i < coordinates.size(); i++) {
            neighbors[i] = coordinates.get(i);
        }
    }

    /*
     * Given a coordinate, calculate its euclidean distance to the rover's goal coordinate
     * (using the distance formula derived from the Pythagorean theorem).
     */
    public double getDistanceToGoal(Coordinate coord) {
        int x1 = coord.getX();
        int y1 = coord.getY();
        int x2 = rover.getEndPosition().getX();
        int y2 = rover.getEndPosition().getY();

        double radicand = (Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));
        double distance = Math.sqrt(radicand);

        return distance;
    }


}
