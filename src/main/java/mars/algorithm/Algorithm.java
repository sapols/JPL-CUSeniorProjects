package mars.algorithm;

import mars.coordinate.Coordinate;
import mars.rover.MarsRover;
import mars.out.Output;
import mars.map.TerrainMap;

import java.util.ArrayList;

/**
 * Abstract class from which all path-finding algorithms inherit.
 */
public abstract class Algorithm {

    public TerrainMap map;
    public MarsRover rover;
    public String outputClass;

    public abstract void findPath() throws Exception;

    public abstract ArrayList<? extends Coordinate> getPath();

}
