package mars.algorithm;

import mars.coordinate.AStarCoordinate;
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

    ArrayList<Coordinate> path = new ArrayList<Coordinate>();
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
        path.add(new Coordinate(rover.getXPosition(),rover.getYPosition()));

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
    public void GreedySearch(ArrayList<Coordinate> coords) throws Exception {
        boolean working = true; //controls main loop that iterates every position change
        boolean stepped; //controls secondary loop that iterates for every candidate position to change to
        ArrayList<GreedyNode> preferences = new ArrayList<GreedyNode>(); //list of candidate positions to change to
        GreedyNode currentNode = new GreedyNode(coords.get(0)); //current position (represented by a node)
        GreedyNode checkNode; //node to check against the current one
        Coordinate currentCoord = currentNode.getPosition(); //coordinates of currentNode
        double goalDirection = 0.; //angle that points to where the goal is (heuristic)
        while(working){ //main loop
            System.out.println(currentCoord.getX() + "," + currentCoord.getY()); //debug
            currentNode.setVisited(true); //we've visited this node

            if(currentNode.getNorthNeighbor() == null && currentCoord.getY() > 0) { //initialize uninitialized neighbors
                Coordinate newCoord = new Coordinate(currentCoord.getX(),currentCoord.getY()-1);
                currentNode.setNorthNeighbor(new GreedyNode(newCoord));
                if(checkArray(newCoord,coords)){currentNode.getNorthNeighbor().setVisited(true);} //if we've already been to this node, this instance of it should be visited.
            }
            if(currentNode.getWestNeighbor() == null && currentCoord.getX() > 0){
                Coordinate newCoord = new Coordinate(currentCoord.getX()-1,currentCoord.getY());
                currentNode.setWestNeighbor(new GreedyNode(newCoord));
                if(checkArray(newCoord,coords)){currentNode.getWestNeighbor().setVisited(true);}
            }
            if(currentNode.getEastNeighbor() == null && currentCoord.getX() < map.getWidth()){
                Coordinate newCoord = new Coordinate(currentCoord.getX()+1,currentCoord.getY());
                currentNode.setEastNeighbor(new GreedyNode(newCoord));
                if(checkArray(newCoord,coords)){currentNode.getEastNeighbor().setVisited(true);}
            }
            if(currentNode.getSouthNeighbor() == null && currentCoord.getY() < map.getHeight()){
                Coordinate newCoord = new Coordinate(currentCoord.getX(),currentCoord.getY()+1);
                currentNode.setSouthNeighbor(new GreedyNode(newCoord));
                if(checkArray(newCoord,coords)){currentNode.getSouthNeighbor().setVisited(true);}
            }

            goalDirection = getAngleToGoal(currentCoord, rover.getEndPosition());
            if(goalDirection > 315 || goalDirection < 45) { //if closest to east
                preferences.add(currentNode.getEastNeighbor());
                if (goalDirection > 315) { // check if next closest to north or south
                    preferences.add(currentNode.getNorthNeighbor());
                    preferences.add(currentNode.getSouthNeighbor());
                } else {
                    preferences.add(currentNode.getSouthNeighbor());
                    preferences.add(currentNode.getNorthNeighbor());
                }
                preferences.add(currentNode.getWestNeighbor());
            }else if(goalDirection >= 45 && goalDirection < 135){ //if closest to south
                preferences.add(currentNode.getSouthNeighbor());
                if(goalDirection < 90){
                    preferences.add(currentNode.getEastNeighbor());
                    preferences.add(currentNode.getWestNeighbor());
                } else {
                    preferences.add(currentNode.getWestNeighbor());
                    preferences.add(currentNode.getEastNeighbor());
                }
                preferences.add(currentNode.getNorthNeighbor());
            }else if(goalDirection >= 135 && goalDirection < 225) { //west
                preferences.add(currentNode.getWestNeighbor());
                if (goalDirection < 180) {
                    preferences.add(currentNode.getSouthNeighbor());
                    preferences.add(currentNode.getNorthNeighbor());
                } else {
                    preferences.add(currentNode.getNorthNeighbor());
                    preferences.add(currentNode.getSouthNeighbor());
                }
                preferences.add(currentNode.getEastNeighbor());
            }else{ //north
                preferences.add(currentNode.getNorthNeighbor());
                if(goalDirection < 270){
                    preferences.add(currentNode.getWestNeighbor());
                    preferences.add(currentNode.getEastNeighbor());
                } else {
                    preferences.add(currentNode.getEastNeighbor());
                    preferences.add(currentNode.getWestNeighbor());
                }
                preferences.add(currentNode.getSouthNeighbor());
            }

            stepped = true; //using our 4 candidates in order of preference, try each one
            while(stepped){
                if(preferences.size() > 0){
                    checkNode = preferences.get(0); //if we actually were able to make that node, and it has a good slope, and we haven't visited it yet, go there
                    if(checkNode != null && processSlope(currentCoord, checkNode.getPosition(), goalDirection) && !checkNode.isVisited()){
                        currentNode = checkNode;
                        currentCoord = currentNode.getPosition();
                        coords.add(currentCoord);
                        stepped = false;
                    }else{ //else ditch it and try the next one
                        preferences.remove(0);
                    }
                }else{ //if we run out of candidates, we lose
                    throw new Exception("WARNING: A path to the goal could not be found.");
                }
            }
            preferences.clear(); //abandon the leftover candidates

            if(currentCoord.equals(goal)){ //and if we reached the goal, stop
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
     * @return boolean if it found a match of target in list
     */
    public boolean checkArray(Coordinate target, ArrayList<Coordinate> list){
        for (Iterator<Coordinate> i = list.iterator(); i.hasNext();){ //foreach coordinate in list
            Coordinate item = i.next();
            if(target.getX() == item.getX() && target.getY() == item.getY()){
                return true;
            }
        }
        return false; //if we traverse the whole list and didn't find a match, false
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
        double point1height = map.getValue(point1.getX(),point1.getY()); //get the heights of the given points
        double point2height = map.getValue(point2.getX(),point2.getY());
        if(point1height != point2height){ //if the heights aren't the same
            double testPoint1x = point1.getX(); //manually get the components (makes the math a lot easier)
            double testPoint1y = point1.getY();
            double testPoint2x = point2.getX();
            double testPoint2y = point2.getY();
            //while the current expanded point height and original are the same, and points are in bounds
            while(point1height == map.getValue(testPoint1x,testPoint1y) && testPoint1x > 0 &&
                    testPoint1x < map.getWidth() && testPoint1y > 0 && testPoint1y < map.getHeight() ){
                testPoint1x -= Math.cos(angle); //subtract one unit length in the desired angle. note we don't round until the end
                testPoint1y -= Math.sin(angle);
            }
            //then do the same for the second point
            while(point2height == map.getValue(testPoint2x,testPoint2y) && testPoint2x > 0 &&
                    testPoint2x < map.getWidth() && testPoint2y > 0 && testPoint2y < map.getHeight() ){
                testPoint2x += Math.cos(angle);
                testPoint2y += Math.sin(angle);
            }
            //finally, construct our resultant coordinate and throw it over to canTraverse to judge it
            return rover.canTraverse(new Coordinate((int)testPoint1x,(int)testPoint1y),new Coordinate((int)testPoint2x,(int)testPoint2y));
        }else return true; //if they're the same height, then it can just freely go there
    }

    public static class GreedyNode { //node class.
        private Coordinate position;
        private GreedyNode northNeighbor; //neighbors in the grid
        private GreedyNode westNeighbor;
        private GreedyNode eastNeighbor;
        private GreedyNode southNeighbor;
        private boolean visited = false;

        public GreedyNode(Coordinate _position) { position = _position; }

        // getters
        public Coordinate getPosition() { return position; }
        public GreedyNode getEastNeighbor() { return eastNeighbor; }
        public GreedyNode getNorthNeighbor() { return northNeighbor; }
        public GreedyNode getSouthNeighbor() { return southNeighbor; }
        public GreedyNode getWestNeighbor() { return westNeighbor; }
        public boolean isVisited() { return visited; }

        // setters
        public void setEastNeighbor(GreedyNode eastNeighbor) { this.eastNeighbor = eastNeighbor; }
        public void setNorthNeighbor(GreedyNode northNeighbor) { this.northNeighbor = northNeighbor; }
        public void setWestNeighbor(GreedyNode westNeighbor) { this.westNeighbor = westNeighbor; }
        public void setSouthNeighbor(GreedyNode southNeighbor) { this.southNeighbor = southNeighbor; }
        public void setVisited(boolean visited) { this.visited = visited; }
    }

}
