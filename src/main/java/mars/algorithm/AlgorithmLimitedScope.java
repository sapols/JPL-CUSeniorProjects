package mars.algorithm;

import mars.coordinate.*;
import mars.rover.MarsRover;
import mars.out.TerminalOutput;
import mars.map.TerrainMap;

import java.util.ArrayList;

/**
 * Class which implements the path-finding algorithm with a limited field of view.
 */
public class AlgorithmLimitedScope extends Algorithm {

    ArrayList<Coordinate> path = new ArrayList<Coordinate>();

    /*
     * Default constructor for an AlgorithmUnlimitedScopeRecursive.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public AlgorithmLimitedScope(MarsRover r) {
        rover = r;
        map = r.getMap();
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

        output = new TerminalOutput(path);
    }

    public ArrayList<Coordinate> getPath() {
        return path;
    }
}
