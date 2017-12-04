package mars;

public abstract class Rover {
    /**
     * Abstract class from which all rovers inherit.
     *
     * maxSlope: maximum slope the rover can traverse
     * currentPosition: current location of rover
     * startPosition: starting location of rover
     * endPosition: goal location of rover
     * map: terrain map
     * fieldOfView: how far the rover can "see" data from the terrain map. used for suboptimal algorithm
     */
    double maxSlope;
    int[] currentPosition = new int[2];
    int[] startPosition = new int[2];
    int[] endPosition = new int[2];
    GeoTIFF map = new GeoTIFF();
    double fieldOfView;

}
