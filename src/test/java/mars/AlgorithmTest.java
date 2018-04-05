package mars;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mars.algorithm.*;
import mars.algorithm.limited.*;
import mars.algorithm.unlimited.*;
import mars.coordinate.Coordinate;
import mars.rover.MarsRover;

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
        } catch (java.awt.HeadlessException expectedException) { //for travis ci
            if(!desiredOutcome) assertTrue("Found a route it shouldn't have", false);
        } catch (Exception expectedException) {
            if(desiredOutcome) assertTrue(expectedException.getClass().getName(),false);
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

    // Tests for LimitedAStar

    //Test if algorithm can complete a trivial route
    public void testAstarAlgorithmLimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String coordType = "L";
        String mapPath = "src/main/resources/marsMap.tif";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can handle a more complex route
    public void testAstarAlgorithmLimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if unlimited algorithm performs better than limited algorithm
    public void testAstarAlgorithmComparedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm limitedAlgorithm = new LimitedAStar(rover, "TerminalOutput");
        Algorithm unlimitedAlgorithm = new UnlimitedAStarRecursive(rover, "TerminalOutput");
        int limitedLength = (tryAlgorithm(limitedAlgorithm,true)).size();
        int unlimitedLength = (tryAlgorithm(unlimitedAlgorithm,true)).size();
        boolean check = unlimitedLength < limitedLength;
        assertTrue("Limited algorithm performed better",check);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testAStarAlgorithmLimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedAStar(rover, "TerminalOutput");
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

    //Test that algorithm fails with an impossible route
    public void testAstarAlgorithmLimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-5,-5);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    //Test that algorithm fails with an unacceptable field of view
    public void testAstarAlgorithmLimitedBadFieldOfView() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,0);
        Algorithm algorithm = new LimitedAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    //Test that algorithm handles field of view properly (1)
    public void testAstarAlgorithmLimitedCheckIfViewedGood() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        LimitedAStar algorithm = new LimitedAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
        assertTrue("good coordinate was treated as bad", algorithm.checkIfViewed(new Coordinate(11,10)));
    }

    //Test that algorithm handles field of view properly (2)
    public void testAstarAlgorithmLimitedCheckIfViewedBad() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        LimitedAStar algorithm = new LimitedAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
        assertTrue("bad coordinate treated as good", !algorithm.checkIfViewed(new Coordinate(21,10)));
    }

    //Test that algorithm handles field of view properly (3)
    public void testAstarAlgorithmLimitedCheckIfViewedRepeated() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        LimitedAStar algorithm = new LimitedAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
        assertTrue("bad coordinate treated as good", !algorithm.checkIfViewed(new Coordinate(10,15)));
    }

    //Test that algorithm angle calculation is correct
    public void testAstarAlgorithmLimitedGetAngleToGoal() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        LimitedAStar algorithm = new LimitedAStar(rover, "TerminalOutput");
        assertTrue("wrong angle returned", 0 == algorithm.getAngleToGoal(new Coordinate(10,10),new Coordinate(11,10)));
    }


    // Tests for UnlimitedAStarRecursive

    //Test if algorithm can complete a trivial route
    public void testAstarAlgorithmUnlimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedAStarRecursive(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test that algorithm fails with an impossible route
    public void testAstarAlgorithmUnlimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-1,-1);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedAStarRecursive(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    //Test if algorithm can complete a trivial route
    public void testAstarAlgorithmUnlimitedNonRecursiveFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedAStarNonRecursive(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test that algorithm fails with an impossible route
    public void testAstarAlgorithmUnlimitedNonRecursiveFailure() throws Exception {
        Coordinate startCoord = new Coordinate(0, 0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-1, -1);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(0,coordType,startCoord, endCoord, mapPath);
        Algorithm algorithm = new UnlimitedAStarNonRecursive(rover, "TerminalOutput");
        tryAlgorithm(algorithm, false);
    }

    // Tests for Dummy Algorithm


    // Tests for AlgorithmGreedy

    //Test if algorithm can complete a trivial route
    public void testGreedyAlgorithmUnlimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedGreedy(rover,"TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can complete a trivial route (limited)
    public void testGreedyAlgorithmLimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new LimitedGreedy(rover,"TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if unlimited and limited algorithms perform the same with trivial routes
    public void testGreedyAlgorithmComparedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithmUnlimited = new UnlimitedGreedy(rover,"TerminalOutput");
        Algorithm algorithmLimited = new LimitedGreedy(rover,"TerminalOutput");
        int unlimitedLength = (tryAlgorithm(algorithmUnlimited,true)).size();
        int limitedLength = (tryAlgorithm(algorithmLimited,true)).size();
        boolean check = unlimitedLength == limitedLength;
        assertTrue("Limited and unlimited routes are not the same",check);
    }

    //Test if algorithm can handle a more complex route
    public void testGreedyAlgorithmUnlimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedGreedy(rover,"TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testGreedyAlgorithmUnlimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedGreedy(rover,"TerminalOutput");
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

    //Test if unlimited algorithm does not backtrack
    public void testGreedyAlgorithmUnlimitedNoBacktrack() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedGreedy(rover,"TerminalOutput");
        ArrayList<? extends Coordinate> path = tryAlgorithm(algorithm,true);
        Set<Coordinate> pathSet = new HashSet<Coordinate>(path);
        boolean check = path.size() == pathSet.size();
        assertTrue("Path has duplicates",check);
    }

    //Test that algorithm fails with an impossible route
    public void testGreedyAlgorithmUnlimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-1,-1);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedGreedy(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    // Tests for UnlimitedIDAStar

    //Test if algorithm can complete a trivial route
    public void testIDAStarAlgorithmFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedIDAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can handle a more complex route
    public void testIDAStarAlgorithmSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedIDAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testIDAStarAlgorithmValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedIDAStar(rover, "TerminalOutput");
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

    // Tests for UnlimitedIDAStar Limited

    //Test if algorithm can complete a trivial route
    public void testIDAStarAlgorithmLimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedIDAStar(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    // Tests for LimitedBestFirst

    //Test if algorithm can complete a trivial route
    public void testBestFirstAlgorithmLimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBestFirst(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can handle a more complex route
    public void testBestFirstAlgorithmLimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBestFirst(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testBestFirstAlgorithmLimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBestFirst(rover, "TerminalOutput");
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

    //Test if unlimited algorithm does not backtrack
    public void testBestFirstAlgorithmLimitedNoBacktrack() throws Exception{
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBestFirst(rover,"TerminalOutput");
        ArrayList<? extends Coordinate> path = tryAlgorithm(algorithm,true);
        Set<Coordinate> pathSet = new HashSet<Coordinate>(path);
        boolean check = path.size() == pathSet.size();
        assertTrue("Path has duplicates",check);
    }

    //Test that algorithm fails with an impossible route
    public void testBestFirstAlgorithmLimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-5,-5);
        String coordType = "L";
        String mapPath = "src/main/resources/marsMap.tif";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBestFirst(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    // Tests for UnlimitedBestFirst

    //Test if algorithm can complete a trivial route
    public void testBestFirstAlgorithmUnlimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedBestFirst(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can handle a more complex route
    public void testBestFirstAlgorithmUnlimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedBestFirst(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testBestFirstAlgorithmUnlimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedBestFirst(rover, "TerminalOutput");
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

    //Test if unlimited algorithm does not backtrack
    public void testBestFirstAlgorithmUnlimitedNoBacktrack() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedBestFirst(rover,"TerminalOutput");
        ArrayList<? extends Coordinate> path = tryAlgorithm(algorithm,true);
        Set<Coordinate> pathSet = new HashSet<Coordinate>(path);
        boolean check = path.size() == pathSet.size();
        assertTrue("Path has duplicates",check);
    }

    //Test that algorithm fails with an impossible route
    public void testBestFirstAlgorithmUnlimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-5,-5);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedBestFirst(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    // Tests for LimitedBreadthFirstSearch

    //Test if algorithm can complete a trivial route
    public void testBreadthFirstSearchAlgorithmLimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBreadthFirstSearch(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can handle a more complex route
    public void testBreadthFirstSearchAlgorithmLimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBreadthFirstSearch(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testBreadthFirstSearchAlgorithmLimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBreadthFirstSearch(rover, "TerminalOutput");
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

    //Test if unlimited algorithm does not backtrack
    public void testBreadthFirstSearchAlgorithmLimitedNoBacktrack() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBreadthFirstSearch(rover,"TerminalOutput");
        ArrayList<? extends Coordinate> path = tryAlgorithm(algorithm,true);
        Set<Coordinate> pathSet = new HashSet<Coordinate>(path);
        boolean check = path.size() == pathSet.size();
        assertTrue("Path has duplicates",check);
    }

    //Test that algorithm fails with an impossible route
    public void testBreadthFirstSearchAlgorithmLimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-5,-5);
        String coordType = "L";
        String mapPath = "src/main/resources/marsMap.tif";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedBreadthFirstSearch(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    // Tests for UnlimitedBreadthFirstSearch

    //Test if algorithm can complete a trivial route
    public void testBreadthFirstSearchAlgorithmUnlimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedBreadthFirstSearch(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can handle a more complex route
    public void testBreadthFirstSearchAlgorithmUnlimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedBreadthFirstSearch(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testBreadthFirstSearchAlgorithmUnlimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedBreadthFirstSearch(rover, "TerminalOutput");
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

    //Test if unlimited algorithm does not backtrack
    public void testBreadthFirstSearchAlgorithmUnlimitedNoBacktrack() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedBreadthFirstSearch(rover,"TerminalOutput");
        ArrayList<? extends Coordinate> path = tryAlgorithm(algorithm,true);
        Set<Coordinate> pathSet = new HashSet<Coordinate>(path);
        boolean check = path.size() == pathSet.size();
        assertTrue("Path has duplicates",check);
    }

    //Test that algorithm fails with an impossible route
    public void testBreadthFirstSearchAlgorithmUnlimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-5,-5);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedBreadthFirstSearch(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    // Tests for LimitedDijkstra

    //Test if algorithm can complete a trivial route
    public void testDijkstraAlgorithmLimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedDijkstra(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can handle a more complex route
    public void testDijkstraAlgorithmLimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedDijkstra(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testDijkstraAlgorithmLimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedDijkstra(rover, "TerminalOutput");
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

    //Test if unlimited algorithm does not backtrack
    public void testDijkstraAlgorithmLimitedBacktrack() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedDijkstra(rover,"TerminalOutput");
        ArrayList<? extends Coordinate> path = tryAlgorithm(algorithm,true);
        Set<Coordinate> pathSet = new HashSet<Coordinate>(path);
        boolean check = path.size() != pathSet.size();
        assertTrue("Path has no duplicates",check);
    }

    //Test that algorithm fails with an impossible route
    public void testDijkstraAlgorithmLimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-5,-5);
        String coordType = "L";
        String mapPath = "src/main/resources/marsMap.tif";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new LimitedDijkstra(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }

    // Tests for UnlimitedDijkstra

    //Test if algorithm can complete a trivial route
    public void testDijkstraAlgorithmUnlimitedFlatCase() throws Exception{
        Coordinate startCoord = new Coordinate(10,10);
        Coordinate endCoord = new Coordinate(10,20);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(1,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedDijkstra(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm can handle a more complex route
    public void testDijkstraAlgorithmUnlimitedSlopedCase() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedDijkstra(rover, "TerminalOutput");
        tryAlgorithm(algorithm,true);
    }

    //Test if algorithm doesn't skip over points and produces a valid route
    public void testDijkstraAlgorithmUnlimitedValidRoute() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedDijkstra(rover, "TerminalOutput");
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

    //Test if unlimited algorithm does not backtrack
    public void testDijkstraAlgorithmUnlimitedNoBacktrack() throws Exception{
        Coordinate startCoord = new Coordinate(275,205);
        Coordinate endCoord = new Coordinate(250,210);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(45,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedDijkstra(rover,"TerminalOutput");
        ArrayList<? extends Coordinate> path = tryAlgorithm(algorithm,true);
        Set<Coordinate> pathSet = new HashSet<Coordinate>(path);
        boolean check = path.size() == pathSet.size();
        assertTrue("Path has duplicates",check);
    }

    //Test that algorithm fails with an impossible route
    public void testDijkstraAlgorithmUnlimitedFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-5,-5);
        String mapPath = "src/main/resources/marsMap.tif";
        String coordType = "L";
        MarsRover rover = new MarsRover(0,coordType,startCoord,endCoord,mapPath);
        Algorithm algorithm = new UnlimitedDijkstra(rover, "TerminalOutput");
        tryAlgorithm(algorithm,false);
    }



    /*
    //Test that algorithm fails with an impossible route
    public void testIDAStarAlgorithmFailure() throws Exception{
        Coordinate startCoord = new Coordinate(0,0); //this is on an island in the map that the rover can't escape
        Coordinate endCoord = new Coordinate(-5,-5);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(0,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new UnlimitedIDAStar(rover);
        tryAlgorithm(algorithm,false);
    }
    */

    /* optional tests, they don't run in travis or need components travis doesn't support

    public void testGreedyAlgorithmLimitedComparison() throws Exception{
        Coordinate startCoord = new Coordinate(400,131);
        Coordinate endCoord = new Coordinate(450,154);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(25,startCoord,endCoord,mapPath);
        Algorithm algorithmUnlimited = new UnlimitedGreedy(rover));
        Algorithm algorithmLimited = new LimitedGreedy(rover));
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
        Algorithm algorithm = new LimitedAStar(rover);
        tryAlgorithm(algorithm,true);
    }

    public void testGreedyAlgorithmComparedComplexCase() throws Exception{
        Coordinate startCoord = new Coordinate(7568,1507);
        Coordinate endCoord = new Coordinate(7436,3927);
        String mapPath = "src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif";
        MarsRover rover = new MarsRover(30,startCoord,endCoord,mapPath);
        Algorithm algorithmUnlimited = new UnlimitedGreedy(rover));
        Algorithm algorithmLimited = new LimitedGreedy(rover));
        int unlimitedLength = (tryAlgorithm(algorithmUnlimited,true)).size();
        int limitedLength = (tryAlgorithm(algorithmLimited,true)).size();
        boolean check = unlimitedLength == limitedLength;
        assertTrue("Limited and unlimited routes are not the same",check);
    }
    */
}