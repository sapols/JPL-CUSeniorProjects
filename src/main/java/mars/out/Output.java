package mars.out;

import mars.coordinate.*;

import java.util.*;

/**
 * Abstract class from which all output classes inherit.
 *
 */
public abstract class Output {

    List<? extends Coordinate> resultList;

    public abstract void writeToOutput();
}
