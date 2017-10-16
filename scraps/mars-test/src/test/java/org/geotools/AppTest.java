package org.geotools;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest
    extends TestCase
{
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

    public void testInstantiateRoute()
    {
        Route newRoute = new Route();
        newRoute.setStartPoint(10.0);
        assertEquals(10.0,newRoute.getStartPoint());
    }

    public void testHeapSize()
    {
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        boolean testSuccess = false;
        if(heapMaxSize > 2047999999){testSuccess = true;};
        assertTrue((Long.toString(heapMaxSize)),testSuccess);
    }

    public void testReadSmallTIFF() throws Exception
    {
        Route newRoute = new Route();
        String[] testArgs = { "Phobos_ME_HRSC_DEM_Global_2ppd.tif" };
        newRoute.main(testArgs);
        Double result = newRoute.getValue(200.0,200.0);
        assertNotSame(Double.toString(result),0,result);
    }

    public void testReadBigTIFF() throws Exception
    {
        Route newRoute = new Route();
        String[] testArgs = { "Mars_MGS_MOLA_DEM_mosaic_global_463m.tif" };
        newRoute.main(testArgs);
        Double resulttop = newRoute.getValue(0.0,0.0);
        Double resultbottom = newRoute.getValue(0.0,23000.0);
        boolean testSuccess = false;
        if(resultbottom > resulttop){testSuccess = true;}
        assertTrue(Double.toString(resultbottom - resulttop),testSuccess);
    }

}
