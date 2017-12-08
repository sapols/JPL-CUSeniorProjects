package mars.algorithm;

import mars.rover.MarsRover;
import mars.out.Output;
import mars.map.TerrainMap;

/**
 * Abstract class from which all path-finding algorithms inherit.
 */
public abstract class Algorithm {

    TerrainMap map;
    MarsRover rover;
    Output output;

    public abstract void findPath();

}
