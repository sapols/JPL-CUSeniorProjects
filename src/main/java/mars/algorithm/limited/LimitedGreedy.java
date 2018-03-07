package mars.algorithm.limited;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.coordinate.GreedyCoordinate;
import mars.out.MapImageOutput;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Math.abs;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 * Uses an A* search.
 */
public class LimitedGreedy extends Algorithm {


    ArrayList<GreedyCoordinate> path = new ArrayList<GreedyCoordinate>();
    Coordinate goal;
    //String mode;

    /**
     * Default constructor for an AlgorithmGreedy.
     *
     * @param r The rover
     * @param output The output type specified during this algorithm's instantiation
     */
    public LimitedGreedy(MarsRover r, String output) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        outputClass = output;
    }

    /**
     * Second constructor for an AlgorithmGreedy which defaults output to TerminalOutput.
     *
     * @param r The rover
     */
    public LimitedGreedy(MarsRover r) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        outputClass = "TerminalOutput";
    }

    /*
     * Method which starts this search algorithm.
     */
    public void findPath() throws Exception {
        path.add(new GreedyCoordinate(rover.getXPosition(),rover.getYPosition()));

        try {
            GreedySearch(path);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Path getter for testing
     * @return path
     */
    public ArrayList<GreedyCoordinate> getPath() {
        return path;
    }

    /*
     * Find a path from start to goal with hope and blind luck. Then output it.
     * Throw an exception if a path cannot be found.
     */
    public void GreedySearch(ArrayList<GreedyCoordinate> coords) throws Exception {

        ArrayList<GreedyCoordinate> fullcoords = new ArrayList<GreedyCoordinate>(); //combined list of rejected nodes and good nodes
        fullcoords.add(new GreedyCoordinate(rover.getXPosition(),rover.getYPosition()));
        ArrayList<GreedyCoordinate> preferences = new ArrayList<GreedyCoordinate>(); //list of candidate positions to change to
        boolean working = true; //controls main loop that iterates every position change
        boolean stepped; //controls secondary loop that iterates for every candidate position to change to

        GreedyCoordinate currentNode = new GreedyCoordinate(coords.get(0)); //current position (represented by a node)
        GreedyCoordinate checkNode; //node to check against the current one

        //Coordinate currentCoord = currentNode.getCoordinate(); //coordinates of currentNode
        double goalDirection; //angle that points to where the goal is (heuristic)

        while(working){ //main loop
            //System.out.println(currentNode.getX() + "," + currentNode.getY()); //debug
            currentNode.setVisited(true); //we've visited this node

            ArrayList<GreedyCoordinate> neighbors = currentNode.getNeighbors();

            goalDirection = getAngleToGoal(currentNode, rover.getEndPosition());
            ArrayList<GreedyCoordinateWrapper> directionList = new ArrayList<GreedyCoordinateWrapper>();

            for (GreedyCoordinate item : neighbors) { //foreach neighbor coordinate
                if (checkArray(item, fullcoords) > -1) {
                    item.setVisited(true); //check if already visited
                }
                if (item.getDirection() == 0 && goalDirection > 180) { //then get angle diffs for each neighbor
                    directionList.add(new GreedyCoordinateWrapper(item, getAngleDiff((int) goalDirection, 360)));
                } else {
                    directionList.add(new GreedyCoordinateWrapper(item, getAngleDiff((int) goalDirection, item.getDirection())));
                }
            }

            Collections.sort(directionList, new Comparator<GreedyCoordinateWrapper>(){ //sort the angles by least diff
                public int compare(GreedyCoordinateWrapper l, GreedyCoordinateWrapper r){
                    return l.getDiff() > r.getDiff() ? 1 : (l.getDiff() < r.getDiff()) ? -1 : 0;
                }
            });

            for (GreedyCoordinateWrapper item : directionList) {
                preferences.add(item.getCoordinate());
            }

            directionList.clear();

            stepped = true; //using our 4 candidates in order of preference, try each one
            while(stepped){
                if(preferences.size() > 0){
                    checkNode = preferences.get(0); //if we actually were able to make that node, and it has a good slope, and we haven't visited it yet, go there
                    if(checkNode != null && rover.canTraverse(currentNode, checkNode) && !checkNode.isVisited()){
                        currentNode = checkNode;
                        coords.add(currentNode);
                        fullcoords.add(currentNode);
                        stepped = false;
                    }else{ //else ditch it and try the next one
                        preferences.remove(0);
                    }
                }else{ //if we run out of candidates, we backtrack
                    if(coords.size() > 1) {
                        //System.out.println("bt " + currentNode.getX() + "," + currentNode.getY());
                        coords.remove(checkArray(currentNode,coords));
                        currentNode = coords.get(coords.size() - 1);
                        currentNode.setVisited(true);
                        fullcoords.add(currentNode);
                        stepped = false;
                    }else{ //if we can't backtrack, we lose
                        throw new Exception("WARNING: A path to the goal could not be found.");
                    }
                }
            }
            preferences.clear(); //abandon the leftover candidates

            if(currentNode.equals(goal)){ //and if we reached the goal, stop
                //no-op
                coords = fullcoords;
                working = false;
            }
        }
    }

    //----Helper methods-----------------------------------------------------------------------------------------------

    /**
     * Quick function to find diff between angles
     * @param angle1 first angle
     * @param angle2 second angle
     * @return diff in degrees
     */
    public int getAngleDiff(int angle1, int angle2){
        return abs(angle1-angle2);
    }

    /**
     * Gets angle to the goal coordinate, from the current coordinate
     * @param current coordinate at current location
     * @param goal coordinate where you're trying to go
     * @return angle in degrees between them
     */
    public double getAngleToGoal(Coordinate current, Coordinate goal) {
        int xdiff = goal.getX() - current.getX();
        int ydiff = goal.getY() - current.getY();
        double result = Math.toDegrees(Math.atan2(ydiff,xdiff));
        while(result < 0){result += 360;}
        return result;
    }

    /**
     * Traverses an array of coordinates checking for a coordinate that matches the provided target.
     * @param target target coordinate with desired x,y
     * @param list list of coordinate to check
     * @return index of target in list, or -1 if failed
     */
    public int checkArray(GreedyCoordinate target, ArrayList<GreedyCoordinate> list){
        int index = 0;
        for (GreedyCoordinate item : list) { //foreach coordinate in list
            if (target.getX() == item.getX() && target.getY() == item.getY()) {
                return index;
            }
            index++;
        }
        return -1; //if we traverse the whole list and didn't find a match, -1
    }

    public class GreedyCoordinateWrapper {
        private GreedyCoordinate coordinate;
        private int diff;

        public GreedyCoordinateWrapper(GreedyCoordinate _coordinate, int _diff){
            coordinate = _coordinate;
            diff = _diff;
        }

        public GreedyCoordinate getCoordinate(){ return coordinate; }
        public int getDiff(){ return diff; }
    }

}
