package mars.coordinate;

public class BestFirstCoordinate extends Coordinate {


    private BestFirstCoordinate parent;

    /**
     * Main constructor for an BestFirstCoordinate.
     */
    public BestFirstCoordinate(int x, int y) {
        super(x, y);

    }

    /**
     * Second constructor for an BestFirstCoordinate which accepts a Coordinate.
     */
    public BestFirstCoordinate(Coordinate coord) {
        super(coord.getX(), coord.getY());

    }



    public BestFirstCoordinate getParent() { return parent; }

    public void setParent(BestFirstCoordinate p) { parent = p; }

    public String toString() { return "(" + getX() + ", " + getY() + ")"; }




}