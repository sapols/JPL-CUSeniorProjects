package mars.algorithm.limited;

import mars.algorithm.Algorithm;
import mars.coordinate.*;
import mars.out.OutputFactory;
import mars.rover.MarsRover;
import mars.out.TerminalOutput;
import mars.map.TerrainMap;

import java.util.ArrayList;

/**
 * Class which implements the path-finding algorithm with a limited field of view.
 */
public class AlgorithmLimitedScope extends Algorithm {

    ArrayList<Coordinate> path = new ArrayList<Coordinate>();

    /**
     * Default constructor for an AlgorithmLimitedScopeRecursive.
     *
     * @param r The rover
     * @param output The output type specified during this algorithm's instantiation
     */
    public AlgorithmLimitedScope(MarsRover r, String output) {
        rover = r;
        map = r.getMap();
        outputClass = output;
    }

    /**
     * Second constructor for an AlgorithmLimitedScopeRecursive which defaults output to TerminalOutput.
     *
     * @param r The rover
     */
    public AlgorithmLimitedScope(MarsRover r) {
        rover = r;
        map = r.getMap();
        outputClass = "TerminalOutput";
    }

    /**
     * TODO Describe findPath() here
     */
    public void findPath() throws Exception {
        //Call the search algorithm method
        blankAlgorithm();
    }

    //TODO: Implement something
    public void blankAlgorithm() {
        path.add(rover.getStartPosition());
        path.add(rover.getEndPosition());
    }

    public ArrayList<Coordinate> getPath() {
        return path;
    }
}
