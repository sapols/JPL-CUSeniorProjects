package mars;

/**
 * Abstract class from which all rovers inherit.
 *
 * @param "..." //TODO: describe variables
 */
public abstract class Rover {

    double maxSlope;
    int[] currentPosition = new int[2]; //x, y
    int[] startPosition = new int[2]; //x, y
    int[] endPosition = new int[2]; //x, y
    GeoTIFF map = new GeoTIFF();
    double fieldOfView;

}
