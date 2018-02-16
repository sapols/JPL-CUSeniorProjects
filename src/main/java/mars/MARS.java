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
    public static void main(String[] args) throws Exception {
        Coordinate startCoord = new Coordinate(538,191);
        Coordinate endCoord = new Coordinate(208,210);
        String mapPath = "src/test/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff";
        MarsRover rover = new MarsRover(45,startCoord,endCoord,mapPath,3);
        Algorithm algorithm = new AlgorithmLimitedDijkstra(rover);
        algorithm.findPath();

        TerminalInterface ti = new TerminalInterface();
        ti.promptUser();
    }

}
