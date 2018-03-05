package mars.rover;

import mars.coordinate.Coordinate;
import mars.map.GeoTIFF;
import mars.map.TerrainMap;

/**
 * Abstract class from which all rovers inherit.
 *
 * @param "..." //TODO: describe variables
 */
public abstract class Rover {

    double maxSlope;
    Coordinate currentPosition; //x, y
    Coordinate startPosition; //x, y
    String coordType;
    Coordinate endPosition; //x, y
    GeoTIFF map = new GeoTIFF();
    double fieldOfView;

    public abstract double getSlope(int x1, int y1, int x2, int y2) throws Exception;
    public abstract boolean canTraverse(Coordinate point1, Coordinate point2);
    public abstract TerrainMap getMap();
}
