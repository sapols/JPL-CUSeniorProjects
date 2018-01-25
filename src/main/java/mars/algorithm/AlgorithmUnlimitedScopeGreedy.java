package mars.algorithm;

import mars.coordinate.GreedyCoordinate;
import mars.coordinate.Coordinate;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 * Uses an A* search.
 */
public class AlgorithmUnlimitedScopeGreedy extends Algorithm {
    public static final int const_SE = 45;
    public static final int const_SW = 135;
    public static final int const_NW = 225;
    public static final int const_NE = 315;
    //public static final int const_E = 0;
    public static final int const_S = 90;
    public static final int const_W = 180;
    public static final int const_N = 270;

    ArrayList<GreedyCoordinate> path = new ArrayList<GreedyCoordinate>();
    Coordinate goal;

    /*
     * Default constructor for an AlgorithmUnlimitedScopeGreedy.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public AlgorithmUnlimitedScopeGreedy(MarsRover r) {
        rover = r;
        map = r.getMap();
        goal = r.getEndPosition();
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

    /*
     * Find a path from start to goal with hope and blind luck. Then output it.
     * Throw an exception if a path cannot be found.
     */
    public void GreedySearch(ArrayList<GreedyCoordinate> coords) throws Exception {

        ArrayList<GreedyCoordinate> fullcoords = new ArrayList<GreedyCoordinate>(); //combined list of rejected nodes and good nodes
        ArrayList<GreedyCoordinate> preferences = new ArrayList<GreedyCoordinate>(); //list of candidate positions to change to
        boolean working = true; //controls main loop that iterates every position change
        boolean stepped; //controls secondary loop that iterates for every candidate position to change to

        GreedyCoordinate currentNode = new GreedyCoordinate(coords.get(0)); //current position (represented by a node)
        GreedyCoordinate checkNode; //node to check against the current one
        GreedyCoordinate northNeighbor;
        GreedyCoordinate eastNeighbor;
        GreedyCoordinate westNeighbor;
        GreedyCoordinate southNeighbor;
        //Coordinate currentCoord = currentNode.getCoordinate(); //coordinates of currentNode
        double goalDirection; //angle that points to where the goal is (heuristic)

        while(working){ //main loop
            System.out.println(currentNode.getX() + "," + currentNode.getY()); //debug
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

            if(goalDirection > const_NE || goalDirection < const_SE) { //if closest to east
                preferences.add(eastNeighbor);
                if (goalDirection > const_NE) { // check if next closest to north or south
                    preferences.add(northNeighbor);
                    preferences.add(southNeighbor);
                } else {
                    preferences.add(southNeighbor);
                    preferences.add(northNeighbor);
                }
                preferences.add(westNeighbor);
            }else if(goalDirection >= const_SE && goalDirection < const_SW){ //if closest to south
                preferences.add(southNeighbor);
                if(goalDirection < const_S){
                    preferences.add(eastNeighbor);
                    preferences.add(westNeighbor);
                } else {
                    preferences.add(westNeighbor);
                    preferences.add(eastNeighbor);
                }
                preferences.add(northNeighbor);
            }else if(goalDirection >= const_SW && goalDirection < const_NW) { //west
                preferences.add(westNeighbor);
                if (goalDirection < const_W) {
                    preferences.add(southNeighbor);
                    preferences.add(northNeighbor);
                } else {
                    preferences.add(northNeighbor);
                    preferences.add(southNeighbor);
                }
                preferences.add(eastNeighbor);
            }else{ //north
                preferences.add(northNeighbor);
                if(goalDirection < const_N){
                    preferences.add(westNeighbor);
                    preferences.add(eastNeighbor);
                } else {
                    preferences.add(eastNeighbor);
                    preferences.add(westNeighbor);
                }
                preferences.add(southNeighbor);
            }

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
                        System.out.println("bt " + currentNode.getX() + "," + currentNode.getY());
                        coords.remove(checkArray(currentNode,coords));
                        currentNode = coords.get(coords.size() - 1);
                        currentNode.setVisited(true);
                        stepped = false;
                    }else{ //if we can't backtrack, we lose
                        throw new Exception("WARNING: A path to the goal could not be found.");
                    }
                }
            }
            preferences.clear(); //abandon the leftover candidates

            if(currentNode.equals(goal)){ //and if we reached the goal, stop
                output = new TerminalOutput(coords);
                working = false;
            }
        }
    }

    //----Helper methods-----------------------------------------------------------------------------------------------

    /**
     * Gets angle to the goal coordinate, from the current coordinate
     * @param current coordinate at current location
     * @param goal coordinate where you're trying to go
     * @return angle in degrees between them
     */
    public double getAngleToGoal(Coordinate current, Coordinate goal) {
        int xdiff = goal.getX() - current.getX();
        int ydiff = goal.getY() - current.getY();
        return Math.toDegrees(Math.atan2(ydiff,xdiff));
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

}
