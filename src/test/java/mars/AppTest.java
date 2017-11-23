package mars;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    //@Test
    public void testRover_initialize() {
        int[] starts = { 10,10 };
        int[] ends = { 20,20 };
        MarsRover newRover = new MarsRover(0,starts,ends,"");
        assertEquals(10,(newRover.getStartPosition())[0]);
    }

    //@Test
    public void testHeapSize() {
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        boolean testSuccess = false;
        if(heapMaxSize > 2047999999){testSuccess = true;}
        assertTrue((Long.toString(heapMaxSize)),testSuccess);
    }

    //@Test
    public void testReadSmallTIFF() throws Exception {
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        Double result = newMap.getValue(200,200);
        assertNotSame(Double.toString(result),0,result);
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
