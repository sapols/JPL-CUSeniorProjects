package mars.coordinate;

/**
 * Wrapper class for an (X, Y) coordinate.
 *
 * TODO: make this abstract and extend for different units?
 */
public class Coordinate implements Comparable<Coordinate> {
    private int x;
    private int y;
    private String units = "pixels";
    private double distanceToGoal;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        distanceToGoal = Double.MAX_VALUE; //initialize to infinity
    }

    public int getX() {
        return x;
    }

    public void setX(int x) { this.x = x; }

    public int getY() {
        return y;
    }

    public void setY(int y) { this.y = y; }

    public String getUnits() {
        return units;
    }

    public void setDistanceToGoal(double d) {
        distanceToGoal = d;
    }

    //----For sorting based on distance to a goal----------------------------------------------------------------------

    /*
     * compareTo should return < 0 if this is supposed to be
     * less than other, > 0 if this is supposed to be greater than
     * other and 0 if they are supposed to be equal
     */
    public int compareTo(Coordinate other) {
        if (this.distanceToGoal < other.distanceToGoal) {
            return -1;
        }
        else if (this.distanceToGoal > other.distanceToGoal) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
