package mars;

import java.util.ArrayList;

/**
 * Abstract class from which all output classes inherit.
 *
 */
public abstract class Output {

    ArrayList<int[]> resultList;

    public abstract void writeToOutput();
}
