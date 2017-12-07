package mars.ui;

//import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;

import mars.algorithm.Algorithm;
import mars.out.Output;
import mars.rover.Rover;

/**
 * Abstract class from which all user interfaces inherit.
 */
public abstract class UserInterface {

    Algorithm algorithm;
    Rover rover;
    Output output;

    public abstract void promptUser();

    public abstract void startAlgorithm();
}
