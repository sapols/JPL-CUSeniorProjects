package mars;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mars.map.GeoTIFF;

public class MapTest extends TestCase{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MapTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MapTest.class );
    }

    //@Test
    public void testGeotiffGetValueValid() throws Exception {
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/main/resources/marsMap.tif");
        Double result = newMap.getValue(200,200);
        assertNotSame(Double.toString(result),0,result);
    }

    //@Test
    public void testGeotiffGetValueBad() throws Exception {
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/main/resources/marsMap.tif");
        try {
            Double result = newMap.getValue(-1,-1);
        } catch (Exception expectedException) {
            assertEquals("Bad getValue",expectedException.getMessage());
        }
    }

    public void testGeotiffMaxHeight() throws Exception{
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/main/resources/marsMap.tif");
        Double result = newMap.getMaxValue();
        assertEquals(result, result);
    }

    public void testGeotiffMinHeight() throws Exception{
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/main/resources/marsMap.tif");
        Double result = newMap.getMinValue();
        assertEquals(result, result);
    }

    public void testGeotiffMinlessthanMax() throws Exception{
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/main/resources/marsMap.tif");
        Double minresult = newMap.getMinValue();
        Double maxresult = newMap.getMaxValue();
        assertTrue(minresult < maxresult);
    }

    /*
    //@Test
    public void testReadBigTIFF() throws Exception {
        Route newRoute = new Route();
        String[] testArgs = { "src/main/resources/Mars_MGS_MOLA_DEM_mosaic_global_463m.tif" }; //file should be in src/main/resources
        newRoute.main(testArgs);
        Double resulttop = newRoute.getValue(0.0,0.0);
        Double resultbottom = newRoute.getValue(0.0,23000.0);
        boolean testSuccess = false;
        if(resultbottom > resulttop){testSuccess = true;}
        assertTrue(Double.toString(resultbottom - resulttop),testSuccess);
    }
    */
}
