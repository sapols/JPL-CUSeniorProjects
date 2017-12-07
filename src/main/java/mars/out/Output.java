package mars.out;

import mars.coordinate.Coordinate;

import java.util.ArrayList;

/**
 * Abstract class from which all output classes inherit.
 *
 */
public abstract class Output {

    ArrayList<Coordinate> resultList;

    public abstract void writeToOutput();
}
