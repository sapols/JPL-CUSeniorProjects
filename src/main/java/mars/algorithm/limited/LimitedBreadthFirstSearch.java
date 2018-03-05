package mars.algorithm.limited;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.rover.MarsRover;

import java.lang.reflect.Array;
import java.util.*;


/**
 * Class which implements the path-finding algorithm without a limited field of view
 */
public class LimitedBreadthFirstSearch extends Algorithm {

	ArrayList<Coordinate> fullPath = new ArrayList<Coordinate>();
	Coordinate goal; //ultimate goal
	Coordinate interimGoal; //goal used to handle iterations of a*
	double fieldOfView;

	/**
	 * Default constructor for an UnlimitedAStarNonRecursive.
	 *
	 * @param r The rover
	 * @param output The output type specified during this algorithm's instantiation
	 */
	public LimitedBreadthFirstSearch(MarsRover r, String output) {
		rover = r;
		map = rover.getMap();
		goal = r.getEndPosition();
		fieldOfView = r.getFieldOfView();
		outputClass = output;
	}

	/**
	 * Second constructor for an UnlimitedAStarNonRecursive which defaults output to "TerminalOutput".
	 *
	 * @param r The rover
	 */
	public LimitedBreadthFirstSearch(MarsRover r) {
		rover = r;
		map = rover.getMap();
		goal = r.getEndPosition();
		fieldOfView = r.getFieldOfView();
		outputClass = "TerminalOutput";
	}

	public ArrayList<Coordinate> getPath() {return fullPath;}

	public void findPath() throws Exception {
        if(fieldOfView < 3) throw new Exception("WARNING: Field of view should be set to 3 or higher."); //interim goal calculations don't work with 1 or 2
        fullPath.add(rover.getStartPosition()); //start coord
        try {
            bfsSearch(fullPath);
        } catch (Exception e) {
            throw e;
        }
    }

    public void bfsSearch(ArrayList<Coordinate> coords) throws Exception {
	    Coordinate thisCoord = coords.get(0);
	    Coordinate backCoord;
	    int backtrackDistance = 0;
	    double goalAngle;
	    ArrayList<Coordinate> tempPath = new ArrayList<Coordinate>();
        while(!thisCoord.equals(goal)){ //while we haven't reached the goal yet
            goalAngle = getAngleToGoal(thisCoord, goal);
            if(getDistanceToPoint(thisCoord,goal) > fieldOfView){ //if the rover can't see the goal...
                interimGoal = new Coordinate((int) (thisCoord.getX() + ((fieldOfView) * Math.cos(Math.toRadians(goalAngle)))),
                        (int) (thisCoord.getY() + ((fieldOfView) * Math.sin(Math.toRadians(goalAngle))))); //then come up with a waypoint it can see in the right direction
            }else{ //if we're close enough to see the goal just use that
                interimGoal = goal;
            }
            try {
                tempPath = bfs(thisCoord,interimGoal); //try a* from our current location to the next waypoint
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
            System.out.println((thisCoord.getX()) + "," + (thisCoord.getY())); //debug
        }
        //If we reached here, we got out of the while loop. We're done!
    }

	/**
	 *Implementation of Breadth First Search
	 */
	public ArrayList<Coordinate> bfs(Coordinate startPosition, Coordinate endPosition) throws Exception{

		Node startNode = new Node(startPosition);
		Node goalNode = new Node(endPosition);

		// Create a queue for BFS
		LinkedList<Node> queue = new LinkedList<Node>();
		List<Node> visitedList = new ArrayList<Node>();

		//set all vertices to be equal to not visited
		queue.add(startNode);

		while (queue.size() != 0) {

			//dequeue and print
			Node currentNode = queue.remove();
			visitedList.add(currentNode);

			if(currentIsGoal(currentNode, goalNode)) {
				ArrayList<Coordinate> path = constructPath(currentNode);
                Collections.reverse(path);
                return path;
			}

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it

			// Get the list of neighbor nodes.
			List<Node> neighborList = getNeighbors(currentNode);
			// For each neighbor node...
			for (Node neighbor : neighborList) {
				// Ignore neighbors if it's too steep and we can't go there.
				if(!rover.canTraverse(currentNode.getPosition(), neighbor.getPosition())) {
					continue;
				}
				// Ignore neighbors if we've already evaluated them.
				if(isNodeInList(neighbor, visitedList)) {
					continue;
				}
				// If we're at an undiscovered node...
				if(!isNodeInList(neighbor, queue)) {
					queue.add(neighbor);
				}

				neighbor.setParent(currentNode);
			}
		}
		Node targetNode = new Node(startPosition);
		for(Node n : visitedList){
		    if(getDistanceToPoint(n.getPosition(),endPosition) < getDistanceToPoint(targetNode.getPosition(),endPosition)){
		        targetNode = n;
            }
        }
        if(getDistanceToPoint(targetNode.getPosition(),endPosition) < (fieldOfView-1)){
            ArrayList<Coordinate> path = constructPath(targetNode);
            Collections.reverse(path);
            return path;
        }else{
            throw new Exception("WARNING: A path to the goal could not be found.");
        }
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
	 * Check if there is a node matching ours in a list
	 * @param node node to test for
	 * @param list list to check
	 * @return boolean whether node is in list
	 */
	private boolean isNodeInList(Node node, List<Node> list) {
		int x = node.getPosition().getX();
		int y = node.getPosition().getY();
		for (Node n : list) {
			int tmpX = n.getPosition().getX();
			int tmpY = n.getPosition().getY();
			if ((x == tmpX) && (y == tmpY)) {return true;}
		}
		return false;
	}

	/**
	 * Constructs a path for BreadthFirst by traversing nodes' parents.
	 * @param currentNode node to start traversing
	 */
	private ArrayList<Coordinate> constructPath(Node currentNode) {
	    ArrayList<Coordinate> newList = new ArrayList<Coordinate>();
		while (currentNode != null) {
			newList.add(currentNode.getPosition());
			currentNode = currentNode.getParent();
		}
		return newList;
	}

	/**
	 * Finds neighbors of a given node
	 * @param currentNode node to check
	 * @return list of nodes neighboring the current node
	 */
	private List<Node> getNeighbors(Node currentNode) {
		// TODO
		// Check if on edge of map - breaks if we're on the pixel right next
		// to the edge.
		List<Node> neighborNodeList = new ArrayList<Node>();

		Coordinate currentCoordinate = currentNode.getPosition();
		int currentX = currentCoordinate.getX();
		int currentY = currentCoordinate.getY();

		// since we're on a grid, treat our graph as such and determine neighbors like that
		Coordinate coordinateLeft = new Coordinate(currentX - 1, currentY);
		Coordinate coordinateUpLeft = new Coordinate(currentX - 1, currentY + 1);
		Coordinate coordinateUp = new Coordinate(currentX, currentY + 1);
		Coordinate coordinateUpRight = new Coordinate(currentX + 1, currentY + 1);
		Coordinate coordinateRight = new Coordinate(currentX + 1, currentY);
		Coordinate coordinateDownRight = new Coordinate(currentX + 1, currentY - 1);
		Coordinate coordinateDown = new Coordinate(currentX, currentY - 1);
		Coordinate coordinateDownLeft = new Coordinate(currentX - 1, currentY - 1);

		Node nodeLeft = new Node(coordinateLeft);
		Node nodeUpLeft = new Node(coordinateUpLeft);
		Node nodeUp = new Node(coordinateUp);
		Node nodeUpRight = new Node(coordinateUpRight);
		Node nodeRight = new Node(coordinateRight);
		Node nodeDownRight = new Node(coordinateDownRight);
		Node nodeDown = new Node(coordinateDown);
		Node nodeDownLeft = new Node(coordinateDownLeft);

		neighborNodeList.add(nodeLeft);
		neighborNodeList.add(nodeUpLeft);
		neighborNodeList.add(nodeUp);
		neighborNodeList.add(nodeUpRight);
		neighborNodeList.add(nodeRight);
		neighborNodeList.add(nodeDownRight);
		neighborNodeList.add(nodeDown);
		neighborNodeList.add(nodeDownLeft);

		Iterator<Node> iter = neighborNodeList.iterator();

		while(iter.hasNext()) {
			Node n = iter.next();
			if (!checkIfViewed(n.position) && !(n.position.equals(interimGoal))) {
				iter.remove();
			}
		}

		return neighborNodeList;
	}

	/**
	 * Check if the two nodes are the same
	 * @param currentNode first node to check
	 * @param goalNode second node to check
	 * @return boolean if the nodes are the same
	 */
	private boolean currentIsGoal(Node currentNode, Node goalNode) {
		double currentX = currentNode.getPosition().getX();
		double currentY = currentNode.getPosition().getY();
		double goalX = goalNode.getPosition().getX();
		double goalY = goalNode.getPosition().getY();

		if ((currentX == goalX) && (currentY == goalY)) {return true;}
		else {return false;}
	}

	public class Node {
		private Coordinate position;
		private Node parent;

		public Node(Coordinate _position) {
			position = _position;
		}
		public Coordinate getPosition() {
			return position;
		}
		public void setPosition(Coordinate _position) {
			position = _position;
		}
		public Node getParent() {
			return parent;
		}
		public void setParent(Node _parent) {
			parent = _parent;
		}
	}

}
