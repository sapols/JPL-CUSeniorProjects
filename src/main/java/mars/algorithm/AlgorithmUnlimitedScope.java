package mars.algorithm;

import mars.coordinate.Coordinate;
import mars.rover.MarsRover;
import mars.out.TerminalOutput;
import mars.map.TerrainMap;

import java.util.ArrayList;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 */
public class AlgorithmUnlimitedScope extends Algorithm {

    ArrayList<Coordinate> path = new ArrayList<Coordinate>();

    /*
     * Default constructor for an AlgorithmUnlimitedScope.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public AlgorithmUnlimitedScope(TerrainMap m, MarsRover r) {
        map = m;
        rover = r;
    }

    public void findPath() {
        //find a path with A*

        //---A "blank" algorithm-----------------------------------
        path.add(rover.getStartPosition());
        path.add(rover.getEndPosition());

        output = new TerminalOutput(path);
        //---------------------------------------------------------
    }

}
