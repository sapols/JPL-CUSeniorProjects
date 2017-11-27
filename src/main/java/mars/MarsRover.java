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

    // determines slope between two points on the elevation map (returns double that is the slope in radians)
    // formally, we find the difference between the elevation (z) of slope1 and slope2, and the xy distance between (x1,y1) and (x2,y2), then construct a right-angle triangle such that:
    //                             /| vertex b (x2,y2,z2)
    //                           /' |
    //                         /'   | zDistance (adj)
    //                       /'     |
    //  vertex a (x1,y1,z1) '-------+
    //               xyDistance (opp)
    // angle A at vertex a equals tan^-1(adj/opp) = atan(zDistance/xyDistance)
    public double getSlope(double x1, double y1, double x2, double y2) throws Exception {
        double z1 = map.getValue(x1,y1);
        double z2 = map.getValue(x2,y2);
        double zDistance = z2 - z1;
        double xyDistance = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
        return Math.atan(zDistance / xyDistance); // construct a right-angle triangle such that adjacent = xyDistance and opposite = zDistance
    }

    // returns true if the rover can traverse to this point, false if it can't. enforces slope requirement.
    public boolean testSlope(double maxSlope, double x1, double y1, double x2, double y2) throws Exception {
        double[] slopes = {-1,-1,-1,-1,-1};
        slopes[0] = getSlope(x1,y1,x2,y2); // traversal slope
        slopes[1] = getSlope(x2,y2,x2+1,y2); //eastern slope
        slopes[2] = getSlope(x2,y2,x2-1,y2); //western slope
        slopes[3] = getSlope(x2,y2,x2,y2+1); //northern slope
        slopes[4] = getSlope(x2,y2,x2,y2-1); //southern slope
        for(double slope : slopes) {
            if (Math.abs(slope) > maxSlope) {
                return false;
            }
        }
        return true;
    }

}
