package mars;

import java.util.ArrayList;

/**
 * Class which implements the path-finding algorithm with a limited field of view.
 */
public class AlgorithmLimitedScope extends Algorithm {

    ArrayList<int[]> path = new ArrayList<int[]>();

    /*
     * Default constructor for an AlgorithmUnlimitedScope.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public AlgorithmLimitedScope(TerrainMap m, MarsRover r) {
        map = m;
        rover = r;
    }

    /**
     * TODO Describe findPath() here
     */
    public void findPath() {
        //find a path

        //---A "blank" algorithm-----------------------------------
        //This commented-out code can be used to ensure that all the pieces connected properly
//        rover.printSpecs();
//        try {
//            System.out.println("Map value at start position: " + map.getValue(rover.getStartPosition()[0], rover.getStartPosition()[1]));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        path.add(rover.getStartPosition());
        path.add(rover.getEndPosition());

        output = new TerminalOutput(path);
        //---------------------------------------------------------
    }
}
