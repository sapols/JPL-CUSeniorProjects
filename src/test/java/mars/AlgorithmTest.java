package mars;

import com.vividsolutions.jts.geom.CoordinateList;
import jdk.nashorn.internal.ir.annotations.Ignore;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mars.algorithm.Algorithm;
import mars.algorithm.AlgorithmGreedy;
import mars.algorithm.AlgorithmLimitedScopeAStar;
import mars.algorithm.AlgorithmUnlimitedScopeRecursive;
import mars.coordinate.Coordinate;
import mars.coordinate.GreedyCoordinate;
import mars.map.GeoTIFF;
import mars.out.FileOutput;
import mars.rover.MarsRover;
import mars.ui.TerminalInterface;

import java.util.*;

import static java.lang.Math.abs;


public class AlgorithmTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AlgorithmTest( String testName )
    {
        super( testName );
    }

    public ArrayList<? extends Coordinate> tryAlgorithm( Algorithm algorithm, boolean desiredOutcome) throws Exception{ //desiredOutcome = true for success, false for failure
        try {
            algorithm.findPath();
            if(!desiredOutcome) assertTrue("Found a route it shouldn't have", false);
        } catch (java.lang.ArrayIndexOutOfBoundsException expectedException) {
            if(!desiredOutcome) assertTrue("Found a route it shouldn't have", false);
        } catch (Exception expectedException) {
            if(desiredOutcome) assertTrue(expectedException.getMessage(),false);
        }
        return algorithm.getPath();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AlgorithmTest.class );
    }

    public void testAstarAlgorithmLimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(1,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new AlgorithmLimitedScopeAStar(rover);
        tryAlgorithm(algorithm,true);
    }

    public void testAstarAlgorithmUnlimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(1,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmUnlimitedScopeRecursive(rover);
        tryAlgorithm(algorithm,true);
    }

    public void testGreedyAlgorithmUnlimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(45,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmGreedy(rover,"unlimited");
        ArrayList<? extends Coordinate> out = tryAlgorithm(algorithm,true);
        Coordinate oldItem;
        Coordinate item = startCoord;
        for (Coordinate aPath : out) { //foreach coordinate in list
            oldItem = item;
            item = aPath;
            int diff = abs(oldItem.getX() - item.getX()) + abs(oldItem.getY() - item.getY());
            if (diff > 2) {
                assertTrue("Path points too far from each other", false);
            }
        }
    }

    public void testAstarAlgorithmComparedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(45,startCoord,endCoord,mapPath,3);
        Algorithm limitedAlgorithm = new AlgorithmLimitedScopeAStar(rover);
        Algorithm unlimitedAlgorithm = new AlgorithmUnlimitedScopeRecursive(rover);
        int limitedLength = (tryAlgorithm(limitedAlgorithm,true)).size();
        int unlimitedLength = (tryAlgorithm(unlimitedAlgorithm,true)).size();
        boolean check = unlimitedLength < limitedLength;
        assertTrue("Limited algorithm performed better",check);
    }

    public void testGreedyAlgorithmUnlimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(1,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmGreedy(rover,"unlimited");
        tryAlgorithm(algorithm,true);
    }

    public void testGreedyAlgorithmLimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(1,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmGreedy(rover,"limited");
        tryAlgorithm(algorithm,true);
    }

    public void testGreedyAlgorithmComparedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(1,startCoord,endCoord,mapPath);
        Algorithm algorithmUnlimited = new AlgorithmGreedy(rover,"unlimited");
        Algorithm algorithmLimited = new AlgorithmGreedy(rover,"limited");
        int unlimitedLength = (tryAlgorithm(algorithmUnlimited,true)).size();
        int limitedLength = (tryAlgorithm(algorithmLimited,true)).size();
        boolean check = unlimitedLength == limitedLength;
        assertTrue("Limited and unlimited routes are not the same",check);
    }

    public void testGreedyAlgorithmUnlimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(45,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmGreedy(rover,"unlimited");
        tryAlgorithm(algorithm,true);
    }

    public void testAstarAlgorithmLimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(45,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new AlgorithmLimitedScopeAStar(rover);
        tryAlgorithm(algorithm,true);
    }

    public void testGreedyAlgorithmUnlimitedNoBacktrack() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(45,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmGreedy(rover,"unlimited");
        ArrayList<? extends Coordinate> path = tryAlgorithm(algorithm,true);
        Set<Coordinate> pathSet = new HashSet<Coordinate>(path);
        boolean check = path.size() == pathSet.size();
        assertTrue("Path has duplicates",check);
    }

    public void testGreedyAlgorithmUnlimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-1,-1);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(0,startCoord,endCoord,mapPath);
        Algorithm algorithm = new AlgorithmGreedy(rover,"unlimited");
        tryAlgorithm(algorithm,false);
    }

    /* optional tests, they don't run in travis or need components travis doesn't support

    public void testGreedyAlgorithmLimitedComparison() throws Exception{
        Coordinate startCoord = new Coordinate(400,131);
        Coordinate endCoord = new Coordinate(450,154);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(25,startCoord,endCoord,mapPath);
        Algorithm algorithmUnlimited = new AlgorithmGreedy(rover,"unlimited");
        Algorithm algorithmLimited = new AlgorithmGreedy(rover,"limited");
        int unlimitedLength = (tryAlgorithm(algorithmUnlimited,true)).size();
        int limitedLength = (tryAlgorithm(algorithmLimited,true)).size();
        boolean check = unlimitedLength != limitedLength;
        assertTrue("Limited and unlimited routes are the same",check);
    }

    public void testGreedyAlgorithmUnlimitedRoverSlopeMatters() throws Exception{
        Coordinate startCoord = new Coordinate(7568,1507);
        Coordinate endCoord = new Coordinate(7467,2625);
        String mapPath = "src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif";
        MarsRover strongRover = new MarsRover(90,startCoord,endCoord,mapPath);
        MarsRover mediumRover = new MarsRover(40,startCoord,endCoord,mapPath);
        MarsRover weakRover = new MarsRover(30,startCoord,endCoord,mapPath);
        Algorithm algorithmStrong = new AlgorithmGreedy(strongRover,"unlimited");
        Algorithm algorithmMedium = new AlgorithmGreedy(mediumRover,"unlimited");
        Algorithm algorithmWeak = new AlgorithmGreedy(weakRover,"unlimited");
        int strongLength = (tryAlgorithm(algorithmStrong,true)).size();
        int mediumLength = (tryAlgorithm(algorithmMedium,true)).size();
        int weakLength = (tryAlgorithm(algorithmWeak,true)).size();
        boolean check = strongLength < mediumLength && mediumLength < weakLength;
        assertTrue("Stronger rovers aren't performing better",check);
    }

    public void testAStarAlgorithmLimited() throws Exception{
        Coordinate startCoord = new Coordinate(7568,1507);
        Coordinate endCoord = new Coordinate(7467,2625);
        //Coordinate startCoord = new Coordinate(538,191);
        //Coordinate endCoord = new Coordinate(208,210);
        //String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        String mapPath = "src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif";
        MarsRover rover = new MarsRover(90 ,startCoord,endCoord,mapPath,20);
        Algorithm algorithm = new AlgorithmLimitedScopeAStar(rover);
        tryAlgorithm(algorithm,true);
    }

    public void testGreedyAlgorithmComparedComplexCase() throws Exception{
        Coordinate startCoord = new Coordinate(7568,1507);
        Coordinate endCoord = new Coordinate(7436,3927);
        String mapPath = "src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif";
        MarsRover rover = new MarsRover(30,startCoord,endCoord,mapPath);
        Algorithm algorithmUnlimited = new AlgorithmGreedy(rover,"unlimited");
        Algorithm algorithmLimited = new AlgorithmGreedy(rover,"limited");
        int unlimitedLength = (tryAlgorithm(algorithmUnlimited,true)).size();
        int limitedLength = (tryAlgorithm(algorithmLimited,true)).size();
        boolean check = unlimitedLength == limitedLength;
        assertTrue("Limited and unlimited routes are not the same",check);
    }
    */
}