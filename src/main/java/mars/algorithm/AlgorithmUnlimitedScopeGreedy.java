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
     * Find a path from start to goal with A*. Then output it.
     * Throw an exception if a path cannot be found.
     */
    public void GreedySearch(ArrayList<Coordinate> coords) throws Exception {
        boolean working = true; //while we're not done
        boolean stepped;
        ArrayList<GreedyNode> preferences = new ArrayList<GreedyNode>();
        GreedyNode currentNode = new GreedyNode(coords.get(0));
        GreedyNode checkNode;
        Coordinate currentCoord = currentNode.getPosition();
        double goalDirection = 0.;
        while(working){
            System.out.println(currentCoord.getX() + "," + currentCoord.getY());
            currentNode.setVisited(true);

            if(currentNode.getNorthNeighbor() == null && currentCoord.getY() > 0) { //initialize uninitialized neighbors
                Coordinate newCoord = new Coordinate(currentCoord.getX(),currentCoord.getY()-1);
                currentNode.setNorthNeighbor(new GreedyNode(newCoord));
                if(checkArray(newCoord,coords)){currentNode.getNorthNeighbor().setVisited(true);}
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
            if(goalDirection > 315 || goalDirection < 45) {
                preferences.add(currentNode.getEastNeighbor());
                if (goalDirection > 315) {
                    preferences.add(currentNode.getNorthNeighbor());
                    preferences.add(currentNode.getSouthNeighbor());
                } else {
                    preferences.add(currentNode.getSouthNeighbor());
                    preferences.add(currentNode.getNorthNeighbor());
                }
                preferences.add(currentNode.getWestNeighbor());
            }else if(goalDirection >= 45 && goalDirection < 135){
                preferences.add(currentNode.getSouthNeighbor());
                if(goalDirection < 90){
                    preferences.add(currentNode.getEastNeighbor());
                    preferences.add(currentNode.getWestNeighbor());
                } else {
                    preferences.add(currentNode.getWestNeighbor());
                    preferences.add(currentNode.getEastNeighbor());
                }
                preferences.add(currentNode.getNorthNeighbor());
            }else if(goalDirection >= 135 && goalDirection < 225) {
                preferences.add(currentNode.getWestNeighbor());
                if (goalDirection < 180) {
                    preferences.add(currentNode.getSouthNeighbor());
                    preferences.add(currentNode.getNorthNeighbor());
                } else {
                    preferences.add(currentNode.getNorthNeighbor());
                    preferences.add(currentNode.getSouthNeighbor());
                }
                preferences.add(currentNode.getEastNeighbor());
            }else{
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

            stepped = true;
            while(stepped){
                if(preferences.size() > 0){
                    checkNode = preferences.get(0);
                    if(checkNode != null && processSlope(currentCoord, checkNode.getPosition(), goalDirection) && !checkNode.isVisited()){
                        currentNode = checkNode;
                        currentCoord = currentNode.getPosition();
                        coords.add(currentCoord);
                        stepped = false;
                    }else{
                        preferences.remove(0);
                    }
                }else{
                    throw new Exception("WARNING: A path to the goal could not be found.");
                }
            }
            preferences.clear();

            if(currentCoord.equals(goal)){
                output = new TerminalOutput(coords);
                working = false;
            }
        }
    }

    //----Helper methods-----------------------------------------------------------------------------------------------

    public double getAngleToGoal(Coordinate current, Coordinate goal) {
        int xdiff = goal.getX() - current.getX();
        int ydiff = goal.getY() - current.getY();
        return Math.toDegrees(Math.atan2(ydiff,xdiff));
    }

    public boolean checkArray(Coordinate target, ArrayList<Coordinate> list){
        for (Iterator<Coordinate> i = list.iterator(); i.hasNext();){
            Coordinate item = i.next();
            if(target.getX() == item.getX() && target.getY() == item.getY()){
                return true;
            }
        }
        return false;
    }

    public boolean processSlope(Coordinate point1, Coordinate point2, double angle) throws Exception{
        double point1height = map.getValue(point1.getX(),point1.getY());
        double point2height = map.getValue(point2.getX(),point2.getY());
        if(point1height != point2height){
            double testPoint1x = point1.getX();
            double testPoint1y = point1.getY();
            double testPoint2x = point2.getX();
            double testPoint2y = point2.getY();
            while(point1height == map.getValue(testPoint1x,testPoint1y) && testPoint1x > 0 &&
                    testPoint1x < map.getWidth() && testPoint1y > 0 && testPoint1y < map.getHeight() ){
                testPoint1x -= Math.cos(angle);
                testPoint1y -= Math.sin(angle);
            }
            while(point2height == map.getValue(testPoint2x,testPoint2y) && testPoint2x > 0 &&
                    testPoint2x < map.getWidth() && testPoint2y > 0 && testPoint2y < map.getHeight() ){
                testPoint2x += Math.cos(angle);
                testPoint2y += Math.sin(angle);
            }

            return rover.canTraverse(new Coordinate((int)testPoint1x,(int)testPoint1y),new Coordinate((int)testPoint2x,(int)testPoint2y));
        }else return true;
    }

    public static class GreedyNode {
        private Coordinate position;
        private GreedyNode northNeighbor;
        private GreedyNode westNeighbor;
        private GreedyNode eastNeighbor;
        private GreedyNode southNeighbor;
        private boolean visited = false;

        public GreedyNode(Coordinate _position) { position = _position; }
        public Coordinate getPosition() { return position; }
        public GreedyNode getEastNeighbor() { return eastNeighbor; }
        public GreedyNode getNorthNeighbor() { return northNeighbor; }
        public GreedyNode getSouthNeighbor() { return southNeighbor; }
        public GreedyNode getWestNeighbor() { return westNeighbor; }
        public boolean isVisited() { return visited; }
        public void setEastNeighbor(GreedyNode eastNeighbor) { this.eastNeighbor = eastNeighbor; }
        public void setNorthNeighbor(GreedyNode northNeighbor) { this.northNeighbor = northNeighbor; }
        public void setWestNeighbor(GreedyNode westNeighbor) { this.westNeighbor = westNeighbor; }
        public void setSouthNeighbor(GreedyNode southNeighbor) { this.southNeighbor = southNeighbor; }
        public void setVisited(boolean visited) { this.visited = visited; }
    }

}
