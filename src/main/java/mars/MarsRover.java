package mars;

/**
 * Represents a rover which traverses a given terrain.
 */
public class MarsRover extends Rover {

    /**
     * Default constructor for the MarsRover class.
     *
     * @param slope the maximum slope that the rover can handle
     * @param startCoords the beginning X, Y position of the rover (passed in as an array).
     * @param endCoords the ending X, Y position of the rover (passed in as an array).
     */
    public MarsRover(double slope, int[] startCoords, int[] endCoords, String mapPath) {
        setMaxSlope(slope);
        setCurrentPosition(startCoords);
        setStartPosition(startCoords);
        setEndPosition(endCoords);
        try {
            map.initTif(mapPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Constructor for a MarsRover with a limited field of view.
     *
     * @param slope The maximum slope that the rover can handle
     * @param startCoords The beginning X, Y position of the rover (passed in as an array).
     * @param endCoords The ending X, Y position of the rover (passed in as an array).
     * @param radius The radius of this rover's field of view
     */
    public MarsRover(double slope, int[] startCoords, int[] endCoords, String mapPath, double radius) {
        setMaxSlope(slope);
        setCurrentPosition(startCoords);
        setStartPosition(startCoords);
        setEndPosition(endCoords);
        setFieldOfView(radius);
        try {
            map.initTif(mapPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //----Getter/Setter Methods----------------------------------------------------------------------------------------

    public void setMaxSlope(double slope) {
        maxSlope = slope;
    }

    public double getMaxSlope() {
        return maxSlope;
    }

    public void setFieldOfView(double radius) { fieldOfView = radius; }

    public double getFieldOfView() { return fieldOfView; }

    public void setCurrentPosition(int[] position) {
        currentPosition[0] = position[0];
        currentPosition[1] = position[1];
    }

    public int[] getCurrentPosition() {
        return currentPosition;
    }

    public void setStartPosition(int[] position) {
        startPosition[0] = position[0];
        startPosition[1] = position[1];
    }

    public int[] getStartPosition() {
        return startPosition;
    }

    public void setEndPosition(int[] position) {
        endPosition[0] = position[0];
        endPosition[1] = position[1];
    }

    public int[] getEndPosition() {
        return endPosition;
    }

    public void setXPosition(int x) {
        currentPosition[0] = x;
    }

    public int getXPosition() {
        return currentPosition[0];
    }

    public void setYPosition(int y) {
        currentPosition[1] = y;
    }

    public int getYPosition() {
        return currentPosition[1];
    }

}
