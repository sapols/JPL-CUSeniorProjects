package mars.algorithm;

import mars.coordinate.Coordinate;
import mars.rover.MarsRover;
import mars.out.TerminalOutput;
import mars.map.TerrainMap;

import java.util.*;

import java.util.ArrayList;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 */
public class BreadthFirstSearch extends Algorithm {

	List<Coordinate> fullPath = new ArrayList<Coordinate>();

	/*
	 * Default constructor for an AlgorithmUnlimitedScopeNonRecursive.
	 *
	 * @param map The terrain map
	 * @param rover The rover
	 */
	public BreadthFirstSearch(MarsRover r) {
		rover = r;
		map = rover.getMap();
	}

	/**
	 * Implementation of BreadthFirstSearch
	 */
	public void findPath() {
		Coordinate startPosition = rover.getStartPosition();
		Coordinate endPosition = rover.getEndPosition();

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
				constructPath(currentNode);
				break;
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
					visitedList.add(currentNode);
				}
				// Ignore neighbors if we've already evaluated them.
				if(isNodeInList(neighbor, queue)) {
					continue;
				}
				// If we're at an undiscovered node...
				if(!isNodeInList(neighbor, queue)) {
					queue.add(neighbor);
				}

				neighbor.setParent(currentNode);
			}
		}

		Collections.reverse(fullPath);
		output = new TerminalOutput(fullPath);
	}

	/**
	 * Check if there is a node matching ours in a list
	 *
	 * @param node node to test for
	 * @param list list to check
	 * @return boolean whether node is in list
	 */
	private boolean isNodeInList(Node node, LinkedList<Node> list) {
		int x = node.getPosition().getX();
		int y = node.getPosition().getY();
		for (Node n : list) {
			int tmpX = n.getPosition().getX();
			int tmpY = n.getPosition().getY();
			if((x == tmpX) && (y == tmpY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds neighbors of a given node
	 *
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

		return neighborNodeList;
	}

	/**
	 * Constructs a path for A* by traversing nodes' parents.
	 *
	 * @param currentNode node to start traversing
	 */
	private void constructPath(Node currentNode) {
		while (currentNode != null) {
			fullPath.add(currentNode.getPosition());
			currentNode = currentNode.getParent();
		}
	}

	private boolean currentIsGoal(Node currentNode, Node goalNode) {
		double currentX = currentNode.getPosition().getX();
		double currentY = currentNode.getPosition().getY();
		double goalX = goalNode.getPosition().getX();
		double goalY = goalNode.getPosition().getY();

		return (currentX == goalX) && (currentY == goalY);
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