package mars.coordinate;

/**
 * Wrapper class for an (X, Y) coordinate which also contains
 * associated cost values for an A* search algorithm.
 */
public class AStarCoordinate extends Coordinate implements Comparable<AStarCoordinate> {
    private double costSoFar;
    private double distanceToGoal;
    private AStarCoordinate parent;

    /**
     * Main constructor for an AStarCoordinate.
     */
    public AStarCoordinate(int x, int y) {
        super(x, y);
        costSoFar = Double.POSITIVE_INFINITY;      //initialize to infinity
        distanceToGoal = Double.POSITIVE_INFINITY; //initialize to infinity
    }

    /**
     * Second constructor for an AStarCoordinate which accepts a Coordinate.
     */
    public AStarCoordinate(Coordinate coord) {
        super(coord.getX(), coord.getY());
        costSoFar = 0; //Note this behavior
        distanceToGoal = Double.POSITIVE_INFINITY;
    }

    public double getCostSoFar() {
        return costSoFar;
    }

    public void setCostSoFar(double c) {
        costSoFar = c;
    }

    public double getDistanceToGoal() {
        return distanceToGoal;
    }

    public void setDistanceToGoal(double d) {
        distanceToGoal = d;
    }

    public AStarCoordinate getParent() { return parent; }

    public void setParent(AStarCoordinate p) { parent = p; }

    public String toString() { return "(" + getX() + ", " + getY() + ")"; }


    //----For sorting based on A* heuristics--------------------------------------------------------------------------

    /**
     * compareTo should return < 0 if this is supposed to be
     * less than other, > 0 if this is supposed to be greater than
     * other and 0 if they are supposed to be equal.
     */
    public int compareTo(AStarCoordinate other) {
        double cost = this.costSoFar + this.distanceToGoal;
        double otherCost = other.costSoFar + other.distanceToGoal;

        if (cost < otherCost) {
            return -1;
        }
        else if (cost > otherCost) {
            return 1;
        }
        else {
            return 0;
        }
    }

}