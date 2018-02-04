package mars.map;

/**
 * Abstract class from which all terrain maps inherit.
 */
public abstract class TerrainMap {

    public abstract void initMap(String fileLocation) throws Exception;

    public abstract double getValue(double x, double y) throws Exception;

    public abstract double getMaxValue() throws Exception;

    public abstract double getMinValue() throws Exception;

    public abstract String getMapPath();

    public abstract double getHeight() throws Exception;

    public abstract double getWidth() throws Exception;

}
