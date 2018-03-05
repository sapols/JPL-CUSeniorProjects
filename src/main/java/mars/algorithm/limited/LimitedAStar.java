package mars.algorithm.limited;

import mars.algorithm.Algorithm;
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
public class LimitedAStar extends Algorithm {

    ArrayList<AStarCoordinate> visitedCoords = new ArrayList<AStarCoordinate>();
    ArrayList<AStarCoordinate> path = new ArrayList<AStarCoordinate>();
    Coordinate goal; //ultimate goal
    Coordinate interimGoal; //goal used to handle iterations of a*
    double fieldOfView;

    /**
     * Default constructor for an LimitedAStar.
     * rover - the rover being input
     * map - the map being used
     * goal - the end position the rover needs to get to
     * fieldOfView - field of view of rover
     *
     * @param r The rover
     * @param output The output type specified during this algorithm's instantiation
     */
    public LimitedAStar(MarsRover r, String output) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        fieldOfView = r.getFieldOfView();
        outputClass = output;
    }

    /**
     * Second constructor for an LimitedAStar which defaults output to "TerminalOutput".
     * rover - the rover being input
     * map - the map being used
     * goal - the end position the rover needs to get to
     * fieldOfView - field of view of rover
     *
     * @param r The rover
     */
    public LimitedAStar(MarsRover r) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        fieldOfView = r.getFieldOfView();
        outputClass = "TerminalOutput";
    }

    /**
     * Method which starts this search algorithm.
     */
    public void findPath() throws Exception {
        if(fieldOfView < 3) throw new Exception("WARNING: Field of view should be set to 3 or higher."); //interim goal calculations don't work with 1 or 2
        path.add(new AStarCoordinate(rover.getStartPosition())); //start coord
        try {
            AStarSearch(path);
        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<AStarCoordinate> getPath() {
        return path;
    }

    /**
     * the primary function for limited a*. manages a* searches
     * @param coords arraylist to store the path
     * @throws Exception n/a
     */
    public void AStarSearch(ArrayList<AStarCoordinate> coords) throws Exception {
        AStarCoordinate thisCoord = coords.get(0); //stores current location. inits as start coord
        AStarCoordinate backCoord; //used for backtracking
        int backtrackDistance = 0; //used to track where to backtrack to
        double goalAngle;
        ArrayList<AStarCoordinate> tempPath = new ArrayList<AStarCoordinate>(); //temporary path to store a* iterations
        while(!thisCoord.equals(goal)){ //while we haven't reached the goal yet
            goalAngle = getAngleToGoal(thisCoord, goal);
            if(getDistanceToPoint(thisCoord,goal) > fieldOfView){ //if the rover can't see the goal...
                interimGoal = new Coordinate((int) (thisCoord.getX() + ((fieldOfView) * Math.cos(Math.toRadians(goalAngle)))),
                        (int) (thisCoord.getY() + ((fieldOfView) * Math.sin(Math.toRadians(goalAngle))))); //then come up with a waypoint it can see in the right direction
            }else{ //if we're close enough to see the goal just use that
                interimGoal = goal;
            }
            tempPath.clear(); //wipe last a* iteration
            visitedCoords.clear();
            tempPath.add(new AStarCoordinate(thisCoord.getX(), thisCoord.getY())); // we need a fresh AStarCoordinate to keep the iterations from seeing each other
            tempPath.get(0).setCostSoFar(0); //set our new start point as such
            try {
                tempPath = AStar(tempPath,interimGoal); //try a* from our current location to the next waypoint
                coords.addAll(tempPath.subList(1,tempPath.size())); //if we got this far, a* worked. add the a* path to the overall path
                backtrackDistance = 0; //reset backtrack distance
            } catch (Exception e) { //if a* failed
                if(coords.get(0).equals(thisCoord)){ //if we've backtracked to the start
                    throw e; //give up
                }else{
                    //System.out.printf("bt"); //backtrack by one. it can't visit thisCoord anymore since it already visited it
                    backtrackDistance++; //first backtrackDistance to get the next backtrack
                    backCoord = coords.get(coords.size() - 1 - backtrackDistance);
                    coords.add(backCoord); //add the backtrack coordinate as the next place.
                    backtrackDistance++; //and a second one to account for the new entry to the overall path
                }
            }

            thisCoord = coords.get(coords.size()-1); //set current location to the latest position in the path
            //System.out.println((thisCoord.getX()) + "," + (thisCoord.getY())); //debug
        }
        //If we reached here, we got out of the while loop. We're done!
    }



    /**
     * Formal implementation of A* that we use for getting from point a to point b.
     * Throw an exception if a path cannot be found.
     * Modified from UnlimitedAStarRecursive
     *
     * @param unvisitedCoords The list of coordinates being considered by the algorithm.
     */
    public ArrayList<AStarCoordinate> AStar(ArrayList<AStarCoordinate> unvisitedCoords, Coordinate currentGoal) throws Exception { //the goal varies, so we take that in as an argument
        if (unvisitedCoords.isEmpty()) { //since we're limited, not making the goal doesn't mean we lose
            AStarCoordinate targetCoord = new AStarCoordinate(0,0);
            for(AStarCoordinate n : visitedCoords){ //find the coord closest to the goal, faster than sorting
                if(n.getDistanceToGoal() < targetCoord.getDistanceToGoal()){
                    targetCoord = n;
                }
            }
            if(targetCoord.getDistanceToGoal() < (fieldOfView-1)) { //if we made at least a bit of progress, take it
                return constructPath(targetCoord);
            }else{ //if we didn't even get close, give up
                throw new Exception("WARNING: A path to the goal could not be found.");
            }
        } else {
            AStarCoordinate thisCoord = unvisitedCoords.get(0);
            visitedCoords.add(thisCoord);

            if (thisCoord.equals(currentGoal)) { //if we found the goal
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
     * @param target coord to check
     * @return boolean if acceptable
     */
    public boolean checkIfViewed(Coordinate target){
        boolean viewed = false;
        for(AStarCoordinate item : path){ //for each item in the overall path (not just for the iteration!)
            if(target.equals(item)){ //if we're considering a coord that's unvisited for the iteration but not the overall run, then fail
                return false; //no repeats allowed
            }
            if(getDistanceToPoint(target,item) <= fieldOfView){ //and if we've seen this coord, it's acceptable
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
            c.setDistanceToGoal(getDistanceToPoint(c,interimGoal)); //use the interim goal since this function is used on an iteration basis
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
