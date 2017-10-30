package mars;

/**
 * Interface which the GeoTiff class implements
 */
public interface IGeoTiff {

    public void initTif(String fileLocation) throws Exception;

    public double getValue(double x, double y) throws Exception;

}
