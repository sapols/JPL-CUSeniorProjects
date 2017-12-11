package mars.out;

import mars.coordinate.*;

import java.util.ArrayList;

/**
 * Abstract class from which all output classes inherit.
 *
 */
public abstract class Output {
    //TODO: MAKE THIS GENERIC AGAIN.
    ArrayList<AStarCoordinate> resultList;

    public abstract void writeToOutput();
}
