package org.geotools;

public class marsTest {
    public static void main(String[] args) throws Exception
    {
        Route newRoute = new Route();
        String[] testArgs = { "Mars_MGS_MOLA_DEM_mosaic_global_463m.tif" };
        newRoute.main(testArgs);
        newRoute.getLine(100);
    }
}
