package mars;

/**
 * Abstract class from which all terrain maps inherit.
 */
public abstract class TerrainMap {

    public abstract void initMap(String fileLocation) throws Exception;

    public abstract double getValue(double x, double y) throws Exception;

}
