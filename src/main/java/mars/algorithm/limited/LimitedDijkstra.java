package mars.algorithm.limited;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.coordinate.DijkstraNode;
import mars.out.MapImageOutput;
import mars.out.TerminalOutput;
import mars.rover.MarsRover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class LimitedDijkstra extends Algorithm {

    ArrayList<Coordinate> fullPath = new ArrayList<Coordinate>();
    ArrayList<Coordinate> path = new ArrayList<Coordinate>();
    ArrayList<DijkstraNode> visitedCoords = new ArrayList<DijkstraNode>();
    DijkstraNode goal;
    DijkstraNode interimGoal; //goal used to handle iterations of dijkstra
    double fieldOfView;

    final int BUFFER_VALUE = 25;

    /*
     * Default constructor for an UnlimitedDijkstra.
     *
     * @param r The rover
     * @param output The output type specified during this algorithm's instantiation
     */
    public LimitedDijkstra(MarsRover r, String output) {
        rover = r;
        map = r.getMap();
        goal = new DijkstraNode(r.getEndPosition());
        fieldOfView = r.getFieldOfView();
        outputClass = output;
    }
    
    public ArrayList<Coordinate> getPath() {
        return fullPath;
    }

    public void findPath() throws Exception{
        if(fieldOfView < 3) throw new Exception("WARNING: Field of view should be set to 3 or higher."); //interim goal calculations don't work with 1 or 2
        fullPath.add(rover.getStartPosition());
        try {
            dijkstraSearch(fullPath);
        } catch (Exception e) {
            throw e;
        }
    }

    public void dijkstraSearch(ArrayList<Coordinate> coords) throws Exception {
        Coordinate thisCoord = coords.get(0); //stores current location. inits as start coord
        Coordinate backCoord; //used for backtracking
        int backtrackDistance = 0; //used to track where to backtrack to
        double goalAngle;
        ArrayList<Coordinate> tempPath = new ArrayList<Coordinate>(); //temporary path to store a* iterations
        DijkstraNode tempNode;
        while(!thisCoord.equals(goal.getPosition())){ //while we haven't reached the goal yet
            goalAngle = getAngleToGoal(thisCoord,goal.getPosition());
            if(getDistanceToPoint(thisCoord,goal.getPosition()) > fieldOfView){ //if the rover can't see the goal...
                interimGoal = new DijkstraNode(new Coordinate((int) (thisCoord.getX() + ((fieldOfView) * Math.cos(Math.toRadians(goalAngle)))),
                        (int) (thisCoord.getY() + ((fieldOfView) * Math.sin(Math.toRadians(goalAngle)))))); //then come up with a waypoint it can see in the right direction
            }else{ //if we're close enough to see the goal just use that
                interimGoal = goal;
            }
            tempPath.clear(); //wipe last a* iteration
            visitedCoords.clear();
            tempNode = new DijkstraNode(new Coordinate(thisCoord.getX(), thisCoord.getY()));
            tempNode.setDistanceFromStart(0); //set our new start point as such
            try {
                tempPath = dijkstra(tempNode,interimGoal); //try a* from our current location to the next waypoint
                coords.addAll(tempPath.subList(1,tempPath.size())); //if we got this far, a* worked. add the a* path to the overall path
                backtrackDistance = 0; //reset backtrack distance
            } catch (Exception e) { //if a* failed
                if(coords.get(0).equals(thisCoord)){ //if we've backtracked to the start
                    throw e; //give up
                }else{
                    //System.out.printf("bt"); //backtrack by one. it can't visit thisCoord anymore since it already visited it
                    backtrackDistance++; //first backtrackDistance to get the next backtrack
                    backCoord = coords.get(coords.size()-1-backtrackDistance);
                    coords.add(backCoord); //add the backtrack coordinate as the next place.
                    backtrackDistance++; //and a second one to account for the new entry to the overall path
                }
            }

            thisCoord = coords.get(coords.size()-1); //set current location to the latest position in the path
            //System.out.println((thisCoord.getX()) + "," + (thisCoord.getY())); //debug
        }
    }

    public ArrayList<Coordinate> dijkstra(DijkstraNode startNode, DijkstraNode goalNode) throws Exception{
        Vector<DijkstraNode> nodeVector = new Vector<DijkstraNode>();
        Vector<DijkstraNode> trashVector = new Vector<DijkstraNode>();

        nodeVector.add(startNode);

        //DijkstraNode startNode = new DijkstraNode(rover.getStartPosition());
        //DijkstraNode goalNode = new DijkstraNode(rover.getEndPosition());

        int startX = startNode.getPosition().getX();
        int startY = startNode.getPosition().getY();

        int goalX = goalNode.getPosition().getX();
        int goalY = goalNode.getPosition().getY();


        // Instead of loading entire map, let's try only loading the square from start to goal with a 100% buffer
        // For example, if start is 10, 10 and goal is 20, 20, the rectangle will have:
        // x vals from 10 - 20, and y vals from 10 - 20.
        int yRange = Math.abs(goalY - startY);
        int xRange = Math.abs(goalX - startX);
        int halfYRange = yRange / 2;
        int halfXRange = xRange / 2;
        int bufferStartY;
        int bufferStartX;
        int bufferGoalY;
        int bufferGoalX;
        if (startX < goalX) {
            bufferStartX = startX - halfXRange;
            bufferStartX = bufferStartX - BUFFER_VALUE < 0 ? 0 : bufferStartX - BUFFER_VALUE;
            bufferGoalX = goalX + halfXRange;
            bufferGoalX += BUFFER_VALUE;
            if (startY < goalY) {
                bufferStartY = startY - halfYRange;
                bufferStartY = bufferStartY - BUFFER_VALUE < 0 ? 0 : bufferStartY - BUFFER_VALUE;
                bufferGoalY = goalY + halfYRange;
                bufferGoalY += BUFFER_VALUE;
                for (int y = bufferStartY; y <= bufferGoalY; y++) {
                    for (int x = bufferStartX; x <= bufferGoalX; x++) {
                        Coordinate tmpCoordinate = new Coordinate(x, y);
                        if(checkIfViewed(tmpCoordinate)) {
                            DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                            tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                            tmpNode.setParent(null);
                            nodeVector.add(tmpNode);
                        }
                    }
                }
            }
            else { // startY > goalY
                bufferStartY = startY + halfYRange;
                bufferStartY += BUFFER_VALUE;
                bufferGoalY = goalY - halfYRange;
                bufferGoalY = bufferGoalY - BUFFER_VALUE < 0 ? 0 : bufferGoalY - BUFFER_VALUE;
                for (int y = bufferGoalY; y <= bufferStartY; y++) {
                    for (int x = bufferStartX; x <= bufferGoalX; x++) {
                        Coordinate tmpCoordinate = new Coordinate(x, y);
                        if(checkIfViewed(tmpCoordinate)) {
                            DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                            tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                            tmpNode.setParent(null);
                            nodeVector.add(tmpNode);
                        }
                    }
                }
            }
        }
        else { // startX > goalX
            bufferStartX = startX + halfXRange;
            bufferStartX += BUFFER_VALUE;
            bufferGoalX = goalX - halfXRange;
            bufferGoalX = bufferGoalX - BUFFER_VALUE < 0 ? 0 : bufferGoalX - BUFFER_VALUE;
            if (startY < goalY) {
                bufferStartY = startY - halfYRange;
                bufferStartY = bufferStartY - BUFFER_VALUE < 0 ? 0 : bufferStartY - BUFFER_VALUE;
                bufferGoalY = goalY + halfYRange;
                bufferGoalY += BUFFER_VALUE;
                for (int y = bufferStartY; y <= bufferGoalY; y++) {
                    for (int x = bufferGoalX; x <= bufferStartX; x++) {
                        Coordinate tmpCoordinate = new Coordinate(x, y);
                        if(checkIfViewed(tmpCoordinate)) {
                            DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                            tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                            tmpNode.setParent(null);
                            nodeVector.add(tmpNode);
                        }
                    }
                }
            }
            else { // startY > goalY
                bufferStartY = startY + halfYRange;
                bufferStartY += BUFFER_VALUE;
                bufferGoalY = goalY - halfYRange;
                bufferGoalY = bufferGoalY - BUFFER_VALUE < 0 ? 0 : bufferGoalY - BUFFER_VALUE;
                for (int y = bufferGoalY; y <= bufferStartY; y++) {
                    for (int x = bufferGoalX; x <= bufferStartX; x++) {
                        Coordinate tmpCoordinate = new Coordinate(x, y);
                        if(checkIfViewed(tmpCoordinate)) {
                            DijkstraNode tmpNode = new DijkstraNode(tmpCoordinate);
                            tmpNode.setDistanceFromStart(Double.POSITIVE_INFINITY);
                            tmpNode.setParent(null);
                            nodeVector.add(tmpNode);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < nodeVector.size(); i++) {
            if (nodeVector.get(i).getPosition().getX() == startX && nodeVector.get(i).getPosition().getY() == startY) {
                nodeVector.get(i).setDistanceFromStart(0);
            }
        }

        while (!nodeVector.isEmpty()) {
            boolean goalFound = false;

            DijkstraNode minNode = getClosestNode(nodeVector);

            if (minNode.getPosition().getX() == Integer.MAX_VALUE) {
                // No path found?
                // System.out.println("INT MAX LOL");
                for(DijkstraNode t : trashVector){
                    if(t.distBetween(goalNode) < minNode.distBetween(goalNode)){
                        minNode = t;
                    }
                }
                if(minNode.distBetween(goalNode) < (fieldOfView-1) && !minNode.getPosition().equals(startNode.getPosition())){
                    List<Coordinate> tmpList = minNode.constructPath();
                    path = new ArrayList<Coordinate>(tmpList);
                    break;
                }else{
                    throw new Exception("WARNING: A path to the goal could not be found.");
                }
            }

            removeNodeFromVector(nodeVector, trashVector, minNode);

            List<DijkstraNode> neighborList = minNode.getNeighbors();
            for (int i = 0; i < neighborList.size(); i++) {

                DijkstraNode currentNode = neighborList.get(i);

                int currentX = currentNode.getPosition().getX();
                int currentY = currentNode.getPosition().getY();

                // Checks to see if current neighbor is in the vector.
                boolean inVector = false;
                for (DijkstraNode nVec : nodeVector) {
                    if (neighborList.get(i).getPosition().getX() == nVec.getPosition().getX() &&
                            neighborList.get(i).getPosition().getY() == nVec.getPosition().getY()){
                        inVector = true;
                    }
                }
                if (inVector || neighborList.get(i).currentIsGoal(goalNode)) {
                    // If current neighbor is in the vector, we're going to get the new distance for that node,
                    // check to see if it's traversable, and if so, set distance and parent for the node.
                    double totalDist = minNode.getDistanceFromStart() + minNode.distBetween(neighborList.get(i));
                    if (totalDist < neighborList.get(i).getDistanceFromStart()) {

                        for (int q = 0; q < nodeVector.size(); q++) {
                            if (nodeVector.get(q).getPosition().getX() == currentX &&
                                    nodeVector.get(q).getPosition().getY() == currentY) {
                                // Alter node within vector.
                                if (rover.canTraverse(minNode.getPosition(), currentNode.getPosition())) {
                                    nodeVector.get(q).setDistanceFromStart(totalDist);
                                    nodeVector.get(q).setParent(minNode);

                                    if (neighborList.get(i).currentIsGoal(goalNode)) {
                                        // Goal node has been found.

                                        for (int w = 0; w < nodeVector.size(); w++) {
                                            if (nodeVector.get(w).getPosition().getX() == neighborList.get(i).getPosition().getX() &&
                                                    nodeVector.get(w).getPosition().getY() == neighborList.get(i).getPosition().getY()) {
                                                // Alter node within vector.
                                                List<Coordinate> tmpList = nodeVector.get(w).constructPath();
                                                path = new ArrayList<Coordinate>(tmpList);
                                                goalFound = true;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

            }
            if (goalFound) {
                break;
            }
        }
        if(nodeVector.isEmpty()){
            DijkstraNode minNode = getClosestNode(trashVector);

            for(DijkstraNode t : trashVector){
                if(t.distBetween(goalNode) < minNode.distBetween(goalNode)){
                    minNode = t;
                }
            }

            if(minNode.distBetween(goalNode) < (fieldOfView-1) && !minNode.getPosition().equals(startNode.getPosition())){
                List<Coordinate> tmpList = minNode.constructPath();
                path = new ArrayList<Coordinate>(tmpList);
            }else{
                throw new Exception("WARNING: A path to the goal could not be found.");
            }
        }
        Collections.reverse(path);
        return path;
    }

    private void removeNodeFromVector(Vector<DijkstraNode> nodeVector, Vector<DijkstraNode> trashVector, DijkstraNode minNode) {
        int minNodeXPos = minNode.getPosition().getX();
        int minNodeYPos = minNode.getPosition().getY();

        for (int i = 0; i < nodeVector.size(); i++) {
            DijkstraNode n = nodeVector.get(i);
            if ((n.getPosition().getX() == minNodeXPos) && (n.getPosition().getY() == minNodeYPos)) {
                trashVector.add(n);
                nodeVector.remove(i);
            }
        }
    }

    private DijkstraNode getClosestNode(Vector<DijkstraNode> nodeVector) {
        double currentMin = Double.POSITIVE_INFINITY;
        DijkstraNode tmp = new DijkstraNode(new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE));
        for (int i = 0; i < nodeVector.size(); i++) {
            DijkstraNode n = nodeVector.get(i);
            if (n.getDistanceFromStart() < currentMin) {
                currentMin = n.getDistanceFromStart();
                tmp = n;
            }
        }
        return tmp;
    }

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
     * boolean to check if a given coordinate is unique and in range of what the rover has seen by this point
     * @param target coord to check
     * @return boolean if acceptable
     */
    public boolean checkIfViewed(Coordinate target){
        boolean viewed = false;
        for(Coordinate item : fullPath){ //for each item in the overall path (not just for the iteration!)
            if(target.equals(item)){ //if we're considering a coord that's unvisited for the iteration but not the overall run, then fail
                return false; //no repeats allowed
            }
            if(getDistanceToPoint(target,item) <= fieldOfView){ //and if we've seen this coord, it's acceptable
                viewed = true; //we now know it's in range, but still have to check for repeats
            }
        }
        return viewed; //if it's in range, true, if not, false
    }

    public ArrayList<Coordinate> convertDijkstraNodeList(ArrayList<DijkstraNode> inCoords){
        ArrayList<Coordinate> outCoords = new ArrayList<Coordinate>();
        for(DijkstraNode item : inCoords){
            outCoords.add(item.getPosition());
        }
        return outCoords;
    }


}
