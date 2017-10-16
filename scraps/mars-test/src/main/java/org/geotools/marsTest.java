package org.geotools;

public class marsTest {
    public static void main(String[] args) throws Exception
    {
        Route newRoute = new Route();
        String[] testArgs = { "Mars_MGS_MOLA_DEM_mosaic_global_463m.tif" };
        newRoute.main(testArgs);
        Double result = newRoute.getValue(200.0,200.0);
        System.out.println(result);
    }
}
