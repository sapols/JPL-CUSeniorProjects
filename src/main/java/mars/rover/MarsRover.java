package mars.rover;

import mars.coordinate.Coordinate;
import mars.map.GeoTIFF;

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
    public MarsRover(double slope, Coordinate startCoords, Coordinate endCoords, String mapPath) {
        setMaxSlope(slope);
        setCurrentPosition(startCoords);
        setStartPosition(startCoords);
        setEndPosition(endCoords);
        try {
            map.initTif(mapPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        fieldOfView = Double.POSITIVE_INFINITY; //"Unlimited"
    }

    /**
     * Constructor for a MarsRover with a limited field of view.
     *
     * @param slope The maximum slope that the rover can handle
     * @param startCoords The beginning X, Y position of the rover (passed in as an array).
     * @param endCoords The ending X, Y position of the rover (passed in as an array).
     * @param radius The radius of this rover's field of view
     */
    public MarsRover(double slope, Coordinate startCoords, Coordinate endCoords, String mapPath, double radius) {
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

    // determines slope between two points on the elevation map (returns double that is the slope in degrees)
    // formally, we find the difference between the elevation (z) of slope1 and slope2, and the xy distance between (x1,y1) and (x2,y2), then construct a right-angle triangle such that:
    //                             /| vertex b (x2,y2,z2)
    //                           /' |
    //                         /'   | zDistance (adj)
    //                       /'     |
    //  vertex a (x1,y1,z1) '-------+
    //               xyDistance (opp)
    // angle A at vertex a equals tan^-1(adj/opp) = atan(zDistance/xyDistance)
    public double getSlope(int x1, int y1, int x2, int y2) throws Exception {
        double z1 = map.getValue(x1,y1);
        double z2 = map.getValue(x2,y2);
        double zDistance = z2 - z1;
        double xyDistance = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
        return Math.toDegrees(Math.atan(zDistance / xyDistance)); // construct a right-angle triangle such that adjacent = xyDistance and opposite = zDistance
    }

    /*
     * Overload of getSlope to allow passing coordinates instead of x's and y's.
     */
    public double getSlope(Coordinate point1, Coordinate point2) throws Exception {
        int x1 = point1.getX();
        int y1 = point1.getY();
        int x2 = point2.getX();
        int y2 = point2.getY();

        double z1 = map.getValue(x1,y1);
        double z2 = map.getValue(x2,y2);
        double zDistance = z2 - z1;
        double xyDistance = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
        return Math.toDegrees(Math.atan(zDistance / xyDistance)); // construct a right-angle triangle such that adjacent = xyDistance and opposite = zDistance
    }

    // returns true if the rover can traverse to this point, false if it can't. enforces slope requirement.
    public boolean canTraverse(Coordinate point1, Coordinate point2) {
        try {
            int x1 = point1.getX();
            int y1 = point1.getY();
            int x2 = point2.getX();
            int y2 = point2.getY();

            double[] slopes = {-1};
            slopes[0] = getSlope(x1, y1, x2, y2); // traversal slope
            for (double slope : slopes) {
                if (Math.abs(slope) > maxSlope) {
                    return false;
                }
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    // Prints out all of the specs of this rover.
    public void printSpecs() {
        System.out.println("\nThe specs of this rover: ");
        System.out.println("Max slope: " + maxSlope);
        System.out.println("Field of view: " + ((fieldOfView==Double.MAX_VALUE) ? "Unlimited" : fieldOfView));
        System.out.println("Current position - X: " + currentPosition.getX() + ", Y: " + currentPosition.getY());
        System.out.println("Start position - X: " + startPosition.getX() + ", Y: " + startPosition.getY());
        System.out.println("End position - X: " + endPosition.getX() + ", Y: " + endPosition.getY());
    }

    //----Getter/Setter Methods----------------------------------------------------------------------------------------

    public GeoTIFF getMap() { return map; }

    public void setMaxSlope(double slope) {
        maxSlope = slope;
    }

    public double getMaxSlope() {
        return maxSlope;
    }

    public void setFieldOfView(double radius) { fieldOfView = radius; }

    public double getFieldOfView() { return fieldOfView; }

    public void setCurrentPosition(Coordinate position) {
        currentPosition = position;
    }

    public Coordinate getCurrentPosition() {
        return currentPosition;
    }

    public void setStartPosition(Coordinate position) {
        startPosition = position;
    }

    public Coordinate getStartPosition() {
        return startPosition;
    }

    public void setEndPosition(Coordinate position) {
        endPosition = position;
    }

    public Coordinate getEndPosition() {
        return endPosition;
    }

    public void setXPosition(int x) { currentPosition.setX(x); }

    public int getXPosition() {
        return currentPosition.getX();
    }

    public void setYPosition(int y) {
        currentPosition.setY(y);
    }

    public int getYPosition() {
        return currentPosition.getY();
    }

}
