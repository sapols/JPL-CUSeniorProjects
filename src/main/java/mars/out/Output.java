package mars.out;

import mars.algorithm.Algorithm;
import mars.coordinate.*;
import mars.map.GeoTIFF;

import java.io.IOException;
import java.util.*;

/**
 * Abstract class from which all output classes inherit.
 *
 */
public abstract class Output {

    List<? extends Coordinate> resultList;
    String coordinateType;
    GeoTIFF convert;

    public abstract void writeToOutput() throws IOException;
}
