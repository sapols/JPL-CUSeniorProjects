package mars;

/**
 * Abstract class from which all path-finding algorithms inherit.
 */
public abstract class Algorithm {

    TerrainMap map;
    Rover rover;

    public abstract void findPath();

}
