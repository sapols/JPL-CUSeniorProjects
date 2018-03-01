package mars;

import mars.algorithm.limited.*;
import mars.algorithm.unlimited.*;
import mars.algorithm.*;
import mars.algorithm.limited.AlgorithmLimitedDijkstra;
import mars.coordinate.Coordinate;
import mars.out.OutputFactory;
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
        TerminalInterface ti = new TerminalInterface();
        ti.promptUser();
    }

}
