package mars.out;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;

import java.io.IOException;
import java.util.List;

/**
 * A "Factory" for returning different output types based on their names.
 */
public class OutputFactory {

    /**
     * Returns Output based on the name of the output class
     * stored in the given algorithm.
     * If an algorithm's "outputClass" contains multiple output types
     * separated by commas, create all of them.
     *
     * @param algorithm The Algorithm whose path will be output
     * @return The corresponding algorithm class(es)
     */
    public static Output getOutput(Algorithm algorithm) {
        String outputClass = algorithm.outputClass;
        List<? extends Coordinate> out = algorithm.getPath();
        String mapPath = algorithm.map.getMapPath();

        if (outputClass.contains(",")) { //multiple Output types requested
            String[] outputs = outputClass.split(",");

            try {
                for (String output : outputs) {
                    if (output.equals("FileOutput")) {
                        new FileOutput(algorithm);
                    } else if (output.equals("MapImageOutput")) {
                        new MapImageOutput(algorithm);
                    } else {
                        new TerminalOutput(algorithm);
                    }
                }
                return new Output() {
                    @Override
                    public void writeToOutput() throws IOException {
                        //no-op to appease Java; return empty Output since all previous Outputs have already been instantiated
                    }
                };
            } catch (IOException ex) {
                //If something weird happened with FileOutput, default to TerminalOutput
                ex.printStackTrace();
                return new TerminalOutput(algorithm);
            }
        }
        else { //only one Output type requested
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

}
