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
     * Default constructor for an AlgorithmUnlimitedScope.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public AlgorithmLimitedScope(MarsRover r) {
        rover = r;
        map = r.getMap();
    }

    public void findPath(ArrayList<? extends Coordinate> coords) throws Exception {
        //find a path

        //---A "blank" algorithm---------------------------------------
        path.add(rover.getStartPosition());
        path.add(rover.getEndPosition());

        output = new TerminalOutput(path);
        //-------------------------------------------------------------
    }
}
