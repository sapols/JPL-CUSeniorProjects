package mars.algorithm;

import mars.coordinate.Coordinate;
import mars.coordinate.AStarCoordinate;
import mars.rover.MarsRover;
import mars.out.Output;
import mars.map.TerrainMap;

import java.util.ArrayList;

/**
 * Abstract class from which all path-finding algorithms inherit.
 */
public abstract class Algorithm {

    TerrainMap map;
    MarsRover rover;
    Output output;

    public abstract void findPath() throws Exception;

    public abstract ArrayList<Coordinate> getPath();

}
