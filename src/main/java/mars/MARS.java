package mars;

import mars.algorithm.*;
import mars.coordinate.Coordinate;
import mars.rover.MarsRover;
import mars.ui.TerminalInterface;

/**
 * This class houses the main method which starts our program.
 */
public class MARS {

    /**
     * Main method to be called to start the program.
     * @param args unused
     */
    public static void main(String[] args) {
        Coordinate startCoord = new Coordinate(7568,1507);
        Coordinate endCoord = new Coordinate(7568,1727);
        String mapPath = "src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif";
        //String mapPath = "src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif";
        MarsRover rover = new MarsRover(15 ,startCoord,endCoord,mapPath,8);
        //Algorithm algorithm = new AlgorithmGreedy(rover,"limited");
        //Algorithm algorithm = new AlgorithmUnlimitedScopeRecursive(rover);
        //Algorithm algorithm = new AlgorithmUnlimitedScopeNonrecursive(rover);
        //Algorithm algorithm = new AlgorithmLimitedScopeAStar(rover);
        Algorithm algorithm = new Dijkstra(rover);
        try {
            algorithm.findPath();
        } catch (Exception expectedException) {
            //assertTrue("Failed to find a route it should have",false);
        }
        boolean check = true;
        //assertTrue(check);

        TerminalInterface ti = new TerminalInterface();
        ti.promptUser();
    }

}
