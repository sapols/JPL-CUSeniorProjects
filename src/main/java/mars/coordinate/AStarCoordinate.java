package mars.coordinate;

/**
 * Wrapper class for an (X, Y) coordinate which also contains
 * associated cost values for an A* search algorithm.
 */
public class AStarCoordinate extends Coordinate implements Comparable<AStarCoordinate> {
    private double costSoFar;
    private double distanceToGoal;

    public AStarCoordinate(int x, int y) {
        super(x, y);
        costSoFar = Double.POSITIVE_INFINITY;      //initialize to infinity
        distanceToGoal = Double.POSITIVE_INFINITY; //initialize to infinity
    }

    public void setCostSoFar(double c) {
        costSoFar = c;
    }

    public void setDistanceToGoal(double d) { distanceToGoal = d; }

    //----For sorting based on A* heuristics--------------------------------------------------------------------------

    /*
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
