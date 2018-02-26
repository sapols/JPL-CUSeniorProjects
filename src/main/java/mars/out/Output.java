package mars.out;

import mars.algorithm.Algorithm;
import mars.coordinate.*;

import java.io.IOException;
import java.util.*;

/**
 * Abstract class from which all output classes inherit.
 *
 */
public abstract class Output {

    List<? extends Coordinate> resultList;
    String coordinateType;

    public abstract void writeToOutput() throws IOException;
}
