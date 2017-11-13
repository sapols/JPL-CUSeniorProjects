package mars;

/**
 * Abstract class from which all terrain maps inherit.
 */
public abstract class TerrainMap {

    abstract void initMap(String fileLocation) throws Exception;

    abstract double getValue(double x, double y) throws Exception;

}
