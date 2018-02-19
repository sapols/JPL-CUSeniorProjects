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
     * Returns an Output based on the name of the output class
     * stored in the given algorithm.
     *
     * @param algorithm The Algorithm whose path will be output.
     * @return The corresponding algorithm class
     */
    public static Output getOutput(Algorithm algorithm) {
        String outputClass = algorithm.outputClass;
        List<? extends Coordinate> out = algorithm.getPath();
        String mapPath = algorithm.map.getMapPath();

        try {
            if (outputClass.equals("FileOutput")) {
                return new FileOutput(algorithm);
            } else if (outputClass.equals("MapImageOutput")) {
                return new MapImageOutput(algorithm);
            } else {
                return new TerminalOutput(algorithm);
            }
        } catch (IOException ex) {
            //If something weird happened with FileOutput, default to TerminalOutput
            ex.printStackTrace();
            return new TerminalOutput(algorithm);
        }
    }

}
