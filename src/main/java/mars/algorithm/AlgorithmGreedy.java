package mars.algorithm;

import mars.coordinate.GreedyCoordinate;
import mars.coordinate.Coordinate;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static java.lang.Math.abs;
import static jdk.nashorn.internal.objects.NativeMath.min;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 * Uses an A* search.
 */
public class AlgorithmGreedy extends Algorithm {
    public static final int const_SE = 45;
    public static final int const_SW = 135;
    public static final int const_NW = 225;
    public static final int const_NE = 315;
    public static final int const_E = 0;
    public static final int const_Emax = 360;
    public static final int const_S = 90;
    public static final int const_W = 180;
    public static final int const_N = 270;

    ArrayList<GreedyCoordinate> path = new ArrayList<GreedyCoordinate>();
    Coordinate goal;
    String mode;

    /*
     * Default constructor for an AlgorithmGreedy.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public AlgorithmGreedy(MarsRover r, String _mode) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
        mode = _mode;
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
    public ArrayList<Coordinate> getPath() {
        ArrayList<Coordinate> convertedPath = new ArrayList<Coordinate>();
        for (Iterator<GreedyCoordinate> i = path.iterator(); i.hasNext();){ //foreach coordinate in list
            GreedyCoordinate item = i.next();
            convertedPath.add(item.getCoordinate());
        }
        return convertedPath;
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
        GreedyCoordinate northNeighbor;
        GreedyCoordinate eastNeighbor;
        GreedyCoordinate westNeighbor;
        GreedyCoordinate southNeighbor;

        GreedyCoordinateWrapper northWrapper;
        GreedyCoordinateWrapper eastWrapper;
        GreedyCoordinateWrapper westWrapper;
        GreedyCoordinateWrapper southWrapper;

        //Coordinate currentCoord = currentNode.getCoordinate(); //coordinates of currentNode
        double goalDirection; //angle that points to where the goal is (heuristic)

        while(working){ //main loop
            //System.out.println(currentNode.getX() + "," + currentNode.getY()); //debug
            currentNode.setVisited(true); //we've visited this node

            northNeighbor = currentNode.getNorthNeighbor();
            eastNeighbor = currentNode.getEastNeighbor();
            westNeighbor = currentNode.getWestNeighbor();
            southNeighbor = currentNode.getSouthNeighbor();

            if(checkArray(northNeighbor,fullcoords) > -1){northNeighbor.setVisited(true);}
            if(checkArray(eastNeighbor,fullcoords) > -1){eastNeighbor.setVisited(true);}
            if(checkArray(westNeighbor,fullcoords) > -1){westNeighbor.setVisited(true);}
            if(checkArray(southNeighbor,fullcoords) > -1){southNeighbor.setVisited(true);}

            goalDirection = getAngleToGoal(currentNode, rover.getEndPosition());

            ArrayList<GreedyCoordinateWrapper> directionList = new ArrayList<GreedyCoordinateWrapper>();
            directionList.add(new GreedyCoordinateWrapper(northNeighbor, getAngleDiff((int)goalDirection,const_N)));
            if(goalDirection <= 180) {
                directionList.add(new GreedyCoordinateWrapper(eastNeighbor, getAngleDiff((int) goalDirection, const_E)));
            }else {
                directionList.add(new GreedyCoordinateWrapper(eastNeighbor, getAngleDiff((int) goalDirection, const_Emax)));
            }
            directionList.add(new GreedyCoordinateWrapper(westNeighbor, getAngleDiff((int)goalDirection,const_W)));
            directionList.add(new GreedyCoordinateWrapper(southNeighbor, getAngleDiff((int)goalDirection,const_S)));

            Collections.sort(directionList, new Comparator<GreedyCoordinateWrapper>(){
                public int compare(GreedyCoordinateWrapper l, GreedyCoordinateWrapper r){
                    return l.getDiff() > r.getDiff() ? 1 : (l.getDiff() < r.getDiff()) ? -1 : 0;
                }
            });

            for (Iterator<GreedyCoordinateWrapper> i = directionList.iterator(); i.hasNext();){
                GreedyCoordinateWrapper item = i.next();
                preferences.add(item.getCoordinate());
            }

            directionList.clear();

            stepped = true; //using our 4 candidates in order of preference, try each one
            while(stepped){
                if(preferences.size() > 0){
                    checkNode = preferences.get(0); //if we actually were able to make that node, and it has a good slope, and we haven't visited it yet, go there
                    if(checkNode != null && processSlope(currentNode, checkNode, getAngleToGoal(currentNode,checkNode)) && !checkNode.isVisited()){
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
                        if(mode.equals("limited")) fullcoords.add(currentNode);
                        stepped = false;
                    }else{ //if we can't backtrack, we lose
                        throw new Exception("WARNING: A path to the goal could not be found.");
                    }
                }
            }
            preferences.clear(); //abandon the leftover candidates

            if(currentNode.equals(goal)){ //and if we reached the goal, stop
                if(mode.equals("limited")){
                    output = new TerminalOutput(fullcoords);
                    coords = fullcoords;
                }else{
                    output = new TerminalOutput(coords);
                }
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
        return (int)abs(angle1-angle2);
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
        for (Iterator<GreedyCoordinate> i = list.iterator(); i.hasNext();){ //foreach coordinate in list
            GreedyCoordinate item = i.next();
            if(target.getX() == item.getX() && target.getY() == item.getY()){
                return index;
            }
            index++;
        }
        return -1; //if we traverse the whole list and didn't find a match, -1
    }

    /**
     * given two points and an angle, expand the distance between the two points along the angle until their respective heights change.
     * @param point1 first point
     * @param point2 second point
     * @param angle angle to expand upon
     * @return if angle is acceptable or not (see canTraverse)
     * @throws Exception thrown by GeoTools
     */
    public boolean processSlope(Coordinate point1, Coordinate point2, double angle) throws Exception{
        double temp1x = point1.getX(); //manually get the components (makes the math a lot easier)
        double temp1y = point1.getY();
        double temp2x = point2.getX();
        double temp2y = point2.getY();

        if(temp1x < 0 || temp2x < 0 || temp1x > map.getWidth() || temp2x > map.getWidth()
                || temp1y < 0 || temp2y < 0 || temp1y > map.getHeight() | temp2y > map.getHeight() )
            return false;

        double point1height = map.getValue(point1.getX(),point1.getY()); //get the heights of the given points
        double point2height = map.getValue(point2.getX(),point2.getY());
        if(point1height != point2height){ //if the heights aren't the same
            //while the current expanded point height and original are the same, and points are in bounds
            while(temp1x > 0 && temp1x < map.getWidth() && temp1y > 0 && temp1y < map.getHeight() ){
                if(point1height != map.getValue(temp1x,temp1y)) break;
                temp1x -= Math.cos(angle); //subtract one unit length in the desired angle. note we don't round until the end
                temp1y -= Math.sin(angle);
            }
            //then do the same for the second point
            while(temp2x > 0 && temp2x < map.getWidth() && temp2y > 0 && temp2y < map.getHeight() ){
                if(point2height != map.getValue(temp2x,temp2y)) break;
                temp2x += Math.cos(angle);
                temp2y += Math.sin(angle);
            }
            //finally, construct our resultant coordinate and throw it over to canTraverse to judge it
            return rover.canTraverse(new Coordinate((int)temp1x,(int)temp1y),new Coordinate((int)temp2x,(int)temp2y));
        }else return true; //if they're the same height, then it can just freely go there
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
