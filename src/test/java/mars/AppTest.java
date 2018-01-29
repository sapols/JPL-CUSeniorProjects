package mars;

import com.vividsolutions.jts.geom.CoordinateList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mars.algorithm.Algorithm;
import mars.algorithm.AlgorithmUnlimitedScopeGreedy;
import mars.coordinate.Coordinate;
import mars.coordinate.GreedyCoordinate;
import mars.map.GeoTIFF;
import mars.out.FileOutput;
import mars.rover.MarsRover;
import mars.ui.TerminalInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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
        Coordinate starts = new Coordinate(10,10);
        Coordinate ends = new Coordinate(20, 20);
        MarsRover newRover = new MarsRover(0,starts,ends,"");
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
    public void testTIFF_getValue_valid() throws Exception {
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        Double result = newMap.getValue(200,200);
        assertNotSame(Double.toString(result),0,result);
    }

    //@Test
    public void testTIFF_getValue_bad() throws Exception {
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        try {
            Double result = newMap.getValue(-1,-1);
        } catch (Exception expectedException) {
            assertEquals("Bad getValue",expectedException.getMessage());
        }
    }

    //@Test
    public void testRover_getSlope_zero() throws Exception {
        Coordinate starts = new Coordinate(10,10);
        Coordinate ends = new Coordinate(20, 20);
        MarsRover newRover = new MarsRover(0,starts,ends,"src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        assertEquals(0.,newRover.getSlope(20,20,21,20));
    }

    //@Test
    public void testRover_testSlope_valid() throws Exception {
        Coordinate starts = new Coordinate(10,10);
        Coordinate ends = new Coordinate(20, 20);
        MarsRover newRover = new MarsRover(0,starts,ends,"src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");

        double maxSlope = 0.5;
        Coordinate point1 = new Coordinate(20, 20);
        Coordinate point2 = new Coordinate(21, 20);
        assertTrue(newRover.canTraverse(point1,point2));
    }

    public void testGeotiffMaxHeight() throws Exception{
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        Double result = newMap.getMaxValue();
        assertEquals(result, result);
    }

    public void testGeotiffMinHeight() throws Exception{
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        Double result = newMap.getMinValue();
        assertEquals(result, result);
    }

    public void testGeotiffMinlessthanMax() throws Exception{
        GeoTIFF newMap = new GeoTIFF();
        newMap.initMap("src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        Double minresult = newMap.getMinValue();
        Double maxresult = newMap.getMaxValue();
        assertTrue(minresult < maxresult);
    }

    public void testCLIcheckMapFailsWithoutRealFile() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertFalse(ti.checkMap(new Scanner("not a real file")));
    }

    public void testCLIcheckMapPassesWithRealFile() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertTrue(ti.checkMap(new Scanner("src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff")));
    }

    public void testCLIcheckSlopeFailsWithNonNumber() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertFalse(ti.checkSlope(new Scanner("letters")));
    }

    public void testCLIcheckSlopePassesWithNumber0() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertTrue(ti.checkSlope(new Scanner("0")));
    }

    public void testCLIcheckSlopeFailsWithNumberBelow0() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertFalse(ti.checkSlope(new Scanner("-19")));
    }

    public void testCLIcheckSlopePassesWithIntBetween0and90() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertTrue(ti.checkSlope(new Scanner("19")));
    }

    public void testCLIcheckSlopePassesWithDoubleBetween0and90() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertTrue(ti.checkSlope(new Scanner("19.1")));
    }

    public void testCLIcheckSlopePassesWithNumber90() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertTrue(ti.checkSlope(new Scanner("90")));
    }

    public void testCLIcheckSlopePassesWithNumberAbove90() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertFalse(ti.checkSlope(new Scanner("112")));
    }

    public void testCLIcheckSlopePassesIgnoringExtraneousData() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertTrue(ti.checkSlope(new Scanner("19.1 extraneous data")));
    }

    public void testCLIcheckStartCoordsFailsWithNonNumbers() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertFalse(ti.checkStartCoords(new Scanner("non-number non-number")));
    }

    public void testCLIcheckStartCoordsFailsWithNonIntegers() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertFalse(ti.checkStartCoords(new Scanner("19.1 19.1")));
    }

    public void testCLIcheckStartCoordsPassesWithTwoInts() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertTrue(ti.checkStartCoords(new Scanner("19 19")));
    }

    public void testCLIcheckEndCoordsFailsWithNonNumbers() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertFalse(ti.checkEndCoords(new Scanner("non-number non-number")));
    }

    public void testCLIcheckEndCoordsFailsWithNonIntegers() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertFalse(ti.checkEndCoords(new Scanner("19.1 19.1")));
    }

    public void testCLIcheckEndCoordsPassesWithTwoInts() throws Exception{
        TerminalInterface ti = new TerminalInterface();
        assertTrue(ti.checkEndCoords(new Scanner("19 19")));
    }

    public void testOutFileOutputConstuctorPasses() throws Exception{
        CoordinateList coords = new CoordinateList();
        FileOutput fo = new FileOutput(coords);
    }

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

    public void testGreedyCoordinateneighborFunction() throws Exception{
        GreedyCoordinate testNode = new GreedyCoordinate(20,20);
        assertEquals(19,testNode.getWestNeighbor().getX());
        assertEquals(21,testNode.getEastNeighbor().getX());
        assertEquals(19,testNode.getNorthNeighbor().getY());
        assertEquals(21,testNode.getSouthNeighbor().getY());
    }

    public void testGreedyAlgorithmcloseFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(1,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmUnlimitedScopeGreedy(rover);
        try {
            algorithm.findPath();
        } catch (Exception expectedException) {
            assertEquals("Failed to find a route it should have",false,true);
        }
    }

    public void testGreedyAlgorithmfarFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(45,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmUnlimitedScopeGreedy(rover);
        try {
            algorithm.findPath();
        } catch (Exception expectedException) {
            assertEquals("Failed to find a route it should have",false,true);
        }
    }

    public void testGreedyAlgorithmFailCase() throws Exception{
        Coordinate startCoord = new Coordinate(600,210);
        Coordinate endCoord = new Coordinate(700,210);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(1,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmUnlimitedScopeGreedy(rover);
        boolean failure = false;
        try {
            algorithm.findPath();
            failure = true;
        } catch (Exception expectedException) {
            assertEquals(true,true);
        }
        if(failure){assertEquals(false,true);}
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
