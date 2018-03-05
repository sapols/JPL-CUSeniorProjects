package mars;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mars.coordinate.Coordinate;
import mars.out.FileOutput;
import mars.out.MapImageOutput;
import mars.out.TerminalOutput;
import mars.ui.TerminalInterface;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class OutputTest extends TestCase{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OutputTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( OutputTest.class );
    }

    public void testTerminalOutputInstantiate() throws Exception {
        ArrayList<Coordinate> list = new ArrayList<Coordinate>();
        TerminalOutput testOut = new TerminalOutput(list);
        assertNotNull(testOut);
    }

    public void testFileOutputInstantiate() throws Exception {
        ArrayList<Coordinate> list = new ArrayList<Coordinate>();
        FileOutput testOut = new FileOutput(list);
        assertNotNull(testOut);
    }

    public void testFileOutputWrite() throws Exception {
        ArrayList<Coordinate> list = new ArrayList<Coordinate>();
        Coordinate testCoord = new Coordinate(1059039900,1023360986);
        list.add(testCoord);
        FileOutput out = new FileOutput(list);
        File testFile = new File("MARS_output.csv");
        Scanner testScan = new Scanner(testFile);
        testScan.nextLine(); //skip "x,y" row
        String testString = testScan.nextLine();
        testScan.close();
        assertEquals(testString, "1059039900,1023360986");
    }

    public void testMultipleOutputRequestFormatting() throws Exception {
        TerminalInterface ti = new TerminalInterface();
        String outputDir = "src/main/java/mars/out/";
        Map<Integer, String> outputs = ti.findOutputs(outputDir);
        String request = "";

        request = "1";
        Boolean requestIsValid = ti.checkOutputRequest(request, outputs);
        assertTrue(requestIsValid);
        assertEquals("FileOutput", ti.getOutputTypesFromRequest(request, outputs));

        request = "1,2,  3";
        requestIsValid = ti.checkOutputRequest(request, outputs);
        assertTrue(requestIsValid);
        assertEquals("FileOutput,MapImageOutput,TerminalOutput", ti.getOutputTypesFromRequest(request, outputs));

        request = "1, stupid";
        requestIsValid = ti.checkOutputRequest(request, outputs);
        assertFalse(requestIsValid);

        request = "stupid";
        requestIsValid = ti.checkOutputRequest(request, outputs);
        assertFalse(requestIsValid);
    }

    /*
    public void testMapImageOutputInstantiate() throws Exception {
        ArrayList<Coordinate> list = new ArrayList<Coordinate>();
        MapImageOutput testOut = new MapImageOutput(list, "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");
        assertNotNull(testOut);
    }
    */
}
