package mars.algorithm;

import mars.coordinate.AStarCoordinate;
import mars.coordinate.Coordinate;
import mars.out.MapImageOutput;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 * Uses an A* search.
 */
public class AlgorithmLimitedScopeAStar extends Algorithm {

    ArrayList<AStarCoordinate> visitedCoords = new ArrayList<AStarCoordinate>();
    ArrayList<AStarCoordinate> path = new ArrayList<AStarCoordinate>();
    Coordinate goal;
    Coordinate interimGoal;
    AStarCoordinate targetCoord;
    double fieldOfView;

    /**
     * Default constructor for an AlgorithmUnlimitedScopeRecursive.
     * rover - the rover being input
     * map - the map being used
     * goal - the end position the rover needs to get to
     *
     * @param r The rover
     */
    public AlgorithmLimitedScopeAStar(MarsRover r) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        targetCoord = new AStarCoordinate(rover.getStartPosition());
        fieldOfView = r.getFieldOfView();
    }

    /**
     * Method which starts this search algorithm.
     */
    public void findPath() throws Exception {
        path.add(new AStarCoordinate(rover.getStartPosition()));
        try {
            AStarSearch(path);
        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<AStarCoordinate> getPath() {
        return path;
    }

    public void AStarSearch(ArrayList<AStarCoordinate> coords) throws Exception {
        AStarCoordinate thisCoord = coords.get(0);
        double goalAngle;
        ArrayList<AStarCoordinate> tempPath = new ArrayList<AStarCoordinate>();
        while(!thisCoord.equals(goal)){
            goalAngle = getAngleToGoal(thisCoord, goal);
            if(getDistanceToPoint(thisCoord,goal) > fieldOfView) {
                interimGoal = new Coordinate((int) (thisCoord.getX() + ((fieldOfView-1) * Math.cos(Math.toRadians(goalAngle)))),
                        (int) (thisCoord.getY() + ((fieldOfView-1) * Math.sin(Math.toRadians(goalAngle))))); //get next waypoint for A*
            }else{
                interimGoal = goal; //we're close enough to use the regular goal!
            }
            tempPath.clear();
            tempPath.add(new AStarCoordinate(thisCoord.getX(), thisCoord.getY())); // we need a fresh AStarCoordinate to keep the iterations from seeing each other
            try {
                tempPath = AStar(tempPath,interimGoal);
            } catch (Exception e) {
                throw e;
            }
            coords.addAll(tempPath); //todo add a step size so only the first N entries are added to coords
            thisCoord = coords.get(coords.size()-1);
        }
        output = new TerminalOutput(path);
    }



    /**
     * Find a path from start to goal with A*. Then output it.
     * Throw an exception if a path cannot be found.
     *
     * @param unvisitedCoords The list of coordinates being considered by the algorithm.
     */
    public ArrayList<AStarCoordinate> AStar(ArrayList<AStarCoordinate> unvisitedCoords, Coordinate currentGoal) throws Exception {
        if (unvisitedCoords.isEmpty()) {
            throw new Exception("WARNING: A path to the goal could not be found.");
        }
        else {
            AStarCoordinate thisCoord = unvisitedCoords.get(0);
            visitedCoords.add(thisCoord);

            if (thisCoord.equals(currentGoal)) { //if we found the goal
                //targetCoord = thisCoord; //for getPath to reference
                return constructPath(thisCoord);
            }
            else {
                ArrayList<AStarCoordinate> unvisitedNeighbors = getReachableUnvisitedNeighbors(thisCoord);
                for (AStarCoordinate n : unvisitedNeighbors) {
                    if (!coordIsInSet(n, unvisitedCoords)) //Don't add duplicates
                        unvisitedCoords.add(n);
                }
                sortCoordinatesByCost(unvisitedCoords);
                unvisitedCoords.remove(thisCoord);
                return AStar(unvisitedCoords, currentGoal);
            }
        }
    }

    //----Helper methods-----------------------------------------------------------------------------------------------

    /**
     * finds angle between two coordinates
     * @param current first coordinate
     * @param goal second coordinate
     * @return angle to second coordinate (0 = east)
     */
    public double getAngleToGoal(Coordinate current, Coordinate goal) {
        int xdiff = goal.getX() - current.getX();
        int ydiff = goal.getY() - current.getY();
        double result = Math.toDegrees(Math.atan2(ydiff,xdiff));
        while(result < 0){result += 360;}
        return result;
    }

    /**
     * boolean to check if a given coordinate is unique and in range of what the rover has seen by this point
     * @param target
     * @return
     */
    public boolean checkIfViewed(Coordinate target){
        boolean viewed = false;
        for(AStarCoordinate item : path){
            if(target.equals(item)){
                return false; //no repeats allowed
            }
            if(getDistanceToPoint(target,item) <= fieldOfView){
                viewed = true; //we now know it's in range, but still have to check for repeats
            }
        }
        return viewed; //if it's in range, true, if not, false
    }

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
                if (!(i == x && j == y) && checkIfViewed(new Coordinate(i,j))) { //check if valid for limited TODO: first element probably can be removed
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
            c.setDistanceToGoal(getDistanceToPoint(c,interimGoal));
        }

        Collections.sort(coords); //Do the sort, per the "compareTo" method in AStarCoordinate
    }

    /**
     * Given a coordinate, calculate its euclidean distance to the rover's goal coordinate
     * (using the distance formula derived from the Pythagorean theorem).
     */
    public double getDistanceToPoint(Coordinate coord, Coordinate coord2) {
        int x1 = coord.getX();
        int y1 = coord.getY();
        int x2 = coord2.getX();
        int y2 = coord2.getY();

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
