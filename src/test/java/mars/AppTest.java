package mars;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mars.coordinate.Coordinate;
import mars.coordinate.GreedyCoordinate;
import mars.map.GeoTIFF;
import mars.rover.MarsRover;

import java.util.*;


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
    public void testRoverInitialize() {
        Coordinate starts = new Coordinate(10,10);
        Coordinate ends = new Coordinate(20, 20);
        String coordType = "L";
        MarsRover newRover = new MarsRover(0,coordType,starts,ends,"");
        assertEquals(10,(newRover.getStartPosition()).getX());
    }

    //@Test
    public void testHeapSize() {
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        boolean testSuccess = false;
        if(heapMaxSize > 2047999999){testSuccess = true;}
        assertTrue((Long.toString(heapMaxSize)),testSuccess);
    }

    //@Test
    public void testRoverGetSlopeZero() throws Exception {
        Coordinate starts = new Coordinate(10,10);
        Coordinate ends = new Coordinate(20, 20);
        String coordType = "L";
        MarsRover newRover = new MarsRover(0,coordType,starts,ends,"src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        assertEquals(0.,newRover.getSlope(20,20,21,20));
    }

    //@Test
    public void testRoverTestSlopeValid() throws Exception {
        Coordinate starts = new Coordinate(10,10);
        Coordinate ends = new Coordinate(20, 20);
        String coordType = "L";
        MarsRover newRover = new MarsRover(0,coordType,starts,ends,"src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");

        double maxSlope = 0.5;
        Coordinate point1 = new Coordinate(20, 20);
        Coordinate point2 = new Coordinate(21, 20);
        assertTrue(newRover.canTraverse(point1,point2));
    }

//     public void testGeotiffGetElevationsInArea() throws Exception {
//         try {
//             Coordinate origin = new Coordinate(10, 33);
//             int width = 4;
//             int height = 3;
//             String mapPath = "src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif";
//             GeoTIFF map = new GeoTIFF();
//             map.initMap(mapPath);

//             double[][] knownElevations = new double[][] {
//                 {map.getValue(10, 35), map.getValue(11, 35), map.getValue(12, 35), map.getValue(13, 35)},
//                 {map.getValue(10, 34), map.getValue(11, 34), map.getValue(12, 34), map.getValue(13, 34)},
//                 {map.getValue(10, 33), map.getValue(11, 33), map.getValue(12, 33), map.getValue(13, 33)}
//             };
//             double[][] elevations = map.getElevationsInArea(origin, width, height);

//             for (int i = 0; i < height; i++) {
//                 for(int j = 0; j < width; j++) {
//                     assertTrue(elevations[i][j] == knownElevations[i][j]);
//                 }
//             }
//         } catch (Exception e) { fail(); }
//     }

    /*
    public void testOutFileOutputWritesToFile() throws Exception{
        CoordinateList coords = new CoordinateList();
        coords.add(new Coordinate(1,1));
        coords.add(new Coordinate(2,2));
        coords.add(new Coordinate(3,3));
        FileOutput fo = new FileOutput(coords);
        fo.writeToOutput();
    }
    */

    public void testGreedyCoordinateInstantiate() throws Exception{
        Coordinate testCoordinate = new Coordinate(20,20);
        GreedyCoordinate testNode = new GreedyCoordinate(testCoordinate);
        assertTrue(testNode.getX() == testCoordinate.getX());
    }

    public void testGreedyCoordinateNeighborFunction() throws Exception{
        GreedyCoordinate testNode = new GreedyCoordinate(20,20);
        ArrayList<GreedyCoordinate> neighbors = testNode.getNeighbors();
        assertEquals(45,neighbors.get(7).getDirection());
    }


    public void testpixeltoLatLong() throws Exception{
        Coordinate currentCoord = new Coordinate(11000, 5000);
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        double[] degrees = newMap.coordinateConvert(currentCoord);
        try{
            assertTrue(degrees[0] != 0 && degrees[1] != 0);
        } catch (Exception e){
            System.out.println(e);
        }
    }


}
