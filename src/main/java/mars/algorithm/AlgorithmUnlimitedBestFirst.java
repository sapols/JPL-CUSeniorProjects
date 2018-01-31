package mars.algorithm;

import mars.coordinate.AStarCoordinate;
import mars.coordinate.Coordinate;
import mars.rover.MarsRover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlgorithmUnlimitedBestFirst extends Algorithm {

    List<Coordinate> fullPath = new ArrayList<Coordinate>();
    Coordinate goal;

    /*
    * Default constructor for an AlgorithmUnlimitedScopeNonRecursive.
    *
    * @param map The terrain map
    * @param rover The rover
    */
    public AlgorithmUnlimitedBestFirst(MarsRover r) {
        rover = r;
        map = rover.getMap();
        goal = r.getEndPosition();
    }

    /*
     * Implementation of Best First
     */
    public void findPath() {
        Coordinate startPosition = rover.getStartPosition();
        Coordinate endPosition = rover.getEndPosition();

        ArrayList<Coordinate> closed = new ArrayList<Coordinate>();
        ArrayList<Coordinate> open = new ArrayList<Coordinate>();
        open.add(startPosition);

        Coordinate current;
        ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();

        while(! open.isEmpty()){
            current = getLowestFScore(open);
            if( current.getX() == goal.getX() && current.getY() == goal.getY())
            {
                //create path and return
            }
            closed.add(current);
            open.remove(current);
            neighbors = getReachableNeighbors(current);

            for(Coordinate n : neighbors){
                if(!closed.contains(n)){
                    open.add(n);
                }
            }



        }

    }

    //------------------Helper_methods---------------------------------------------------
    /**
     * Given a coordinate, return all the neighboring coordinates
     * which can be visited by this algorithm's rover (meaning that
     * the slope between the coordinates is not too steep).
     * Possible neighbors are all eight coordinates surrounding the given one.
     * @param coord The coordinate whose neighbors will be found.
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




    private double estimateHeuristic(Coordinate current, Coordinate goal) {
        //Chebyshev/Diagonal Distance (allows diagonal movement)


        double currentXPos = current.getX();
        double currentYPos = current.getY();

        double goalXPos = goal.getX();
        double goalYPos = goal.getY();

        return Math.max(Math.abs(currentXPos - goalXPos) , Math.abs(currentYPos - goalYPos));
    }

    private Coordinate getLowestFScore(ArrayList<Coordinate> list) {
        Coordinate lowest = list.get(0);
        for (Coordinate n : list) {
            if (estimateHeuristic(n, goal) < estimateHeuristic(lowest, goal)) {
                lowest = n;
            }
        }
        return lowest;
    }

}
