package mars;

/**
 * Class which implements the path-finding algorithm without a limited field of view.
 */
public class OptimalAlgorithm extends Algorithm {

    /*
     * Default constructor for an OptimalAlgorithm.
     *
     * @param map The terrain map
     * @param rover The rover
     */
    public OptimalAlgorithm(TerrainMap m, Rover r) {
        map = m;
        rover = r;
    }

    public void findPath() {
        //find a path with A* algorithm.
    }

}
