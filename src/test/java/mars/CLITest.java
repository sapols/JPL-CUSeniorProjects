package mars;

import com.vividsolutions.jts.geom.CoordinateList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mars.out.FileOutput;
import mars.ui.TerminalInterface;

import java.util.Scanner;

public class CLITest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CLITest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CLITest.class );
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
}
