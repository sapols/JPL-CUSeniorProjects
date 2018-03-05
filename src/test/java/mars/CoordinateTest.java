package mars;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mars.coordinate.*;

public class CoordinateTest extends TestCase{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CoordinateTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CoordinateTest.class );
    }

    public void testCoordinateInstantiate() throws Exception {
        Coordinate testCoord = new Coordinate(10,10);
        assertNotNull(testCoord);
    }

    public void testAStarCoordinateInstantiate() throws Exception {
        Coordinate coord = new Coordinate(10,10);
        AStarCoordinate testCoord = new AStarCoordinate(coord);
        assertNotNull(testCoord);
    }

    public void testAStarCoordinateMethods() throws Exception {
        AStarCoordinate aStarCoord = new AStarCoordinate(10, 10);
        AStarCoordinate aStarCoord2 = new AStarCoordinate(11, 12);
        aStarCoord2.setCostSoFar(9000);
        aStarCoord2.setDistanceToGoal(1);
        aStarCoord2.setParent(aStarCoord);

        assertNotNull(aStarCoord);
        assertEquals(aStarCoord2.getX(), 11);
        assertEquals(aStarCoord2.getY(), 12);
        assertEquals(aStarCoord.getCostSoFar(), Double.POSITIVE_INFINITY);
        assertEquals(aStarCoord.getDistanceToGoal(), Double.POSITIVE_INFINITY);
        assertEquals(aStarCoord2.getCostSoFar(), 9000.0);
        assertEquals(aStarCoord2.getDistanceToGoal(), 1.0);
        assertEquals(aStarCoord, aStarCoord2.getParent());
    }

    public void testBestFirstCoordinateInstantiate() throws Exception {
        Coordinate coord = new Coordinate(10,10);
        BestFirstCoordinate testCoord = new BestFirstCoordinate(coord);
        assertNotNull(testCoord);
    }

    public void testDijkstraNodeInstantiate() throws Exception {
        Coordinate coord = new Coordinate(10,10);
        DijkstraNode testCoord = new DijkstraNode(coord);
        assertNotNull(testCoord);
    }

    public void testGreedyCoordinateInstantiate() throws Exception {
        Coordinate coord = new Coordinate(10,10);
        GreedyCoordinate testCoord = new GreedyCoordinate(coord);
        assertNotNull(testCoord);
    }
}
