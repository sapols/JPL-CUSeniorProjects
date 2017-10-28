package org.geotools;

public class marsTest {
    public static void main(String[] args) throws Exception {
        Route newRoute = new Route();
        String[] testArgs = { "src/main/resources/Mars_MGS_MOLA_DEM_mosaic_global_463m.tif" }; //file should be in src/main/resources
        newRoute.main(testArgs);
        newRoute.getLine(100);
    }
}
