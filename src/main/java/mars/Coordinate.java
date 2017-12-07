package mars;

/**
 * Wrapper class for an (X, Y) coordinate
 *
 * TODO: make this abstract and extend for units?
 */
public class Coordinate {
    private int x;
    private int y;
    private String units = "pixels";

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getUnits() {
        return units;
    }
}
