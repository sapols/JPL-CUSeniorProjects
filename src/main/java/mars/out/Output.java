package mars.out;

import mars.coordinate.*;

import java.util.ArrayList;

/**
 * Abstract class from which all output classes inherit.
 *
 */
public abstract class Output {

    ArrayList<? extends Coordinate> resultList;

    public abstract void writeToOutput();
}
