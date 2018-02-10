package mars.out;

import mars.algorithm.Algorithm;
import mars.algorithm.limited.AlgorithmGreedy;
import mars.algorithm.limited.AlgorithmLimitedScope;
import mars.algorithm.unlimited.AlgorithmUnlimitedScopeNonRecursive;
import mars.algorithm.unlimited.AlgorithmUnlimitedScopeRecursive;
import mars.algorithm.unlimited.Dijkstra;
import mars.coordinate.Coordinate;
import mars.rover.MarsRover;

import java.io.IOException;
import java.util.List;

/**
 * A "Factory" for returning different output types based on their names.
 */
public class OutputFactory {

    /**
     * Returns an Output class based on its name.
     *
     * @param out The name of the Output class to be returned.
     * @return The corresponding algorithm class
     */
    public static Output getOutputFromName(String outputClass, List<? extends Coordinate> out, String mapPath) {
        try {
            if (outputClass.equals("FileOutput")) {
                return new FileOutput(out, mapPath);
            } else if (outputClass.equals("MapImageOutput")) {
                return new MapImageOutput(out, mapPath);
            } else {
                return new TerminalOutput(out, mapPath);
            }
        } catch (IOException ex) {
            //If something weird happened with FileOutput, default to TerminalOutput
            ex.printStackTrace();
            return new TerminalOutput(out, mapPath);
        }
    }

}
