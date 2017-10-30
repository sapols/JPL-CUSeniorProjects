package mars;

import org.geotools.GeoTIFF;

/**
 * Represents a rover which traverses a given terrain.
 */
public class Rover implements IRover {

    double maxSlope;
    int[] currentPosition = new int[2]; //x, y
    int[] startPosition = new int[2]; //x, y
    int[] endPosition = new int[2]; //x, y
    GeoTIFF terrainMap = new GeoTIFF();

    /**
     * Default constructor for the Rover class.
     *
     * @param slope the maximum slope that the rover can handle
     * @param startCoords the beginning X, Y position of the rover (passed in as an array).
     * @param endCoords the ending X, Y position of the rover (passed in as an array).
     */
    public Rover(double slope, int[] startCoords, int[] endCoords, String mapPath) {
        setMaxSlope(slope);
        setCurrentPosition(startCoords);
        setStartPosition(startCoords);
        setEndPosition(endCoords);
        try {
            terrainMap.initTif(mapPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void findOptimalPath() {
        //Run the algorithm and output path to a file
    }

    public void findSuboptimalPath() {
        //Run the algorithm and output path to a file
    }

    //----Getter/Setter Methods----------------------------------------------------------------------------------------

    public void setMaxSlope(double slope) {
        maxSlope = slope;
    }

    public double getMaxSlope() {
        return maxSlope;
    }

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
