package mars.coordinate;

/**
 * Wrapper class for an (X, Y) coordinate.
 *
 * TODO: Extend this class for different units?
 */
public class Coordinate {
    protected int x;
    protected int y;
    protected String units = "pixels";

    /**
     * Constructor for Coordinate class
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }


    //----Getter/Setter Methods----------------------------------------------------------------------------------------

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getUnits() { return units; }

    public boolean equals(Coordinate other) {
        return ((other.getX()==this.getX()) && (other.getY()==this.getY()));
    }

}
