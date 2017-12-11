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

    //TODO: MAKE THIS GENERIC AGAIN, not just AStarCoordinate
    public abstract void findPath(ArrayList<AStarCoordinate> coords) throws Exception;

}
