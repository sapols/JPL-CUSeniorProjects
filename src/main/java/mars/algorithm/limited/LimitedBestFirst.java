package mars.algorithm.limited;

import mars.algorithm.Algorithm;
import mars.coordinate.BestFirstCoordinate;
import mars.coordinate.Coordinate;
import mars.out.MapImageOutput;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;

import java.util.ArrayList;
import java.util.Collections;


public class LimitedBestFirst extends Algorithm {

    ArrayList<BestFirstCoordinate> path = new ArrayList<BestFirstCoordinate>();
    ArrayList<BestFirstCoordinate> visitedCoords = new ArrayList<BestFirstCoordinate>();
    Coordinate goal;
    Coordinate interimGoal; //goal used to handle iterations of a*
    double fieldOfView;

    /*
    * Default constructor for an UnlimitedAStarNonRecursive.
    *
    * @param r The rover
    * @param output The output type specified during this algorithm's instantiation
    */
    public LimitedBestFirst(MarsRover r, String output) {
        rover = r;
        map = rover.getMap();
        goal = r.getEndPosition();
        fieldOfView = r.getFieldOfView();
        outputClass = output;
    }

    /*
     *   Return path
     */
    public ArrayList<BestFirstCoordinate> getPath() {
        return path;
    }

    public void findPath() throws Exception {
        if(fieldOfView < 3) throw new Exception("WARNING: Field of view should be set to 3 or higher."); //interim goal calculations don't work with 1 or 2
        path.add(new BestFirstCoordinate(rover.getStartPosition())); //start coord
        try {
            BestFirstSearch(path);
        } catch (Exception e) {
            throw e;
        }
    }

    public void BestFirstSearch(ArrayList<BestFirstCoordinate> coords) throws Exception {
        BestFirstCoordinate thisCoord = coords.get(0);
        BestFirstCoordinate backCoord;
        int backtrackDistance = 0;
        double goalAngle;
        ArrayList<BestFirstCoordinate> tempPath = new ArrayList<BestFirstCoordinate>();
        while (!thisCoord.equals(goal)) {
            goalAngle = getAngleToGoal(thisCoord, goal);
            if (getDistanceToPoint(thisCoord, goal) > fieldOfView) {
                interimGoal = new Coordinate((int) (thisCoord.getX() + ((fieldOfView) * Math.cos(Math.toRadians(goalAngle)))),
                        (int) (thisCoord.getY() + ((fieldOfView) * Math.sin(Math.toRadians(goalAngle))))); //then come up with a waypoint it can see in the right direction

            } else {
                interimGoal = goal;
            }
            tempPath.clear();
            try {
                tempPath = BestFirst(new BestFirstCoordinate(thisCoord.getX(), thisCoord.getY()), new BestFirstCoordinate(interimGoal.getX(), interimGoal.getY()));
                coords.addAll(tempPath.subList(1, tempPath.size()));
                backtrackDistance = 0;
            } catch (Exception e) {
                if (coords.get(0).equals(thisCoord)) {
                    throw e;
                } else {
                    backtrackDistance++;
                    backCoord = coords.get(coords.size() - 1 - backtrackDistance);
                    coords.add(backCoord);
                    backtrackDistance++;
                }
            }
            thisCoord = coords.get(coords.size() - 1);
            System.out.println((thisCoord.getX()) + "," + (thisCoord.getY())); //debug
        }
    }

    /*
     * Implementation of Best First
     */
    public ArrayList<BestFirstCoordinate> BestFirst(BestFirstCoordinate startPosition, BestFirstCoordinate endPosition) throws Exception{
        ArrayList<BestFirstCoordinate> closed = new ArrayList<BestFirstCoordinate>();
        ArrayList<BestFirstCoordinate> open = new ArrayList<BestFirstCoordinate>();
        boolean validNeighbor = false;
        open.add(startPosition);

        BestFirstCoordinate current;
        ArrayList<BestFirstCoordinate> neighbors = new ArrayList<BestFirstCoordinate>();

        while(!open.isEmpty()){
            current = getLowestFScore(open);
            if (current.equals(endPosition)) { //if we found the goal
                return constructPath(current);
            }
            closed.add(current);
            open.remove(current);
            neighbors = getReachableNeighbors(current);

            for(BestFirstCoordinate n : neighbors){
                validNeighbor = true;
                for(BestFirstCoordinate m : closed){
                    if(m.equals(n)){
                        validNeighbor = false;
                        break;
                    }
                }
                for(BestFirstCoordinate m : open){
                    if(m.equals(n)){
                        validNeighbor = false;
                        break;
                    }
                }
                if(validNeighbor){
                    open.add(n);
                }
            }
        }
        BestFirstCoordinate targetCoord = new BestFirstCoordinate(0,0);
        double targetDistance = getDistanceToPoint(startPosition,endPosition);
        double newDistance;
        for(BestFirstCoordinate n : closed){
            newDistance = getDistanceToPoint(n,endPosition);
            if(getDistanceToPoint(n,endPosition) < targetDistance){
                targetCoord = n;
                targetDistance = newDistance;
            }
        }
        if(targetDistance < (fieldOfView-1)){
            return constructPath(targetCoord);
        }else {
            throw new Exception("WARNING: A path to the goal could not be found.");
        }
    }

    //------------------Helper_methods---------------------------------------------------

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
        for(BestFirstCoordinate item : path){ //for each item in the overall path (not just for the iteration!)
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
     * @param coord The coordinate whose neighbors will be found.
     */
    public ArrayList<BestFirstCoordinate> getReachableNeighbors(BestFirstCoordinate coord) {
        int x = coord.getX();
        int y = coord.getY();

        ArrayList<BestFirstCoordinate> neighbors = new ArrayList<BestFirstCoordinate>();

        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                if (!(i == x && j == y) && checkIfViewed(new Coordinate(i,j))) { //if this is not the given coordinate "coord"
                    try {
                        BestFirstCoordinate potentialNeighbor = new BestFirstCoordinate(i, j);
                        potentialNeighbor.setParent(coord);

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
     * Heuristic function for BestFirst implementing diagonal distance between two nodes
     * @param current current node
     * @param goal goal node
     * @return diagonal distance
     */

    private double estimateHeuristic(Coordinate current, Coordinate goal) {
        //Chebyshev/Diagonal Distance (allows diagonal movement)


        double currentXPos = current.getX();
        double currentYPos = current.getY();

        double goalXPos = goal.getX();
        double goalYPos = goal.getY();

        return (Math.abs(currentXPos - goalXPos) + Math.abs(currentYPos - goalYPos));
    }

    private BestFirstCoordinate getLowestFScore(ArrayList<BestFirstCoordinate> list) {
        BestFirstCoordinate lowest = list.get(0);
        for (BestFirstCoordinate n : list) {
            if (estimateHeuristic(n, interimGoal) < estimateHeuristic(lowest, interimGoal)) {
                lowest = n;
            }
        }
        return lowest;
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
     * Constructs path using parent nodes
     * @param coord final coordinate in path
     *
     */
    private ArrayList<BestFirstCoordinate> constructPath(BestFirstCoordinate coord){
        ArrayList<BestFirstCoordinate> newPath = new ArrayList<BestFirstCoordinate>();
        while (coord != null){
            newPath.add(coord);
            coord = coord.getParent();
        }
        Collections.reverse(newPath);
        return newPath;
    }

}
