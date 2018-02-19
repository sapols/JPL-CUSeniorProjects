package mars.algorithm;

import mars.algorithm.unlimited.*;
import mars.algorithm.limited.*;
import mars.rover.*;

/**
 * A "Factory" for returning different algorithms based on their names.
 */
public class AlgorithmFactory {

    /**
     * Returns an Algorithm based on which class name is provided.
     *
     * @param algorithmClass The name of the algorithm class to be returned
     * @param rover The rover to be given to the specified algorithm
     * @return The corresponding algorithm class
     */
    public static Algorithm getAlgorithm(String algorithmClass, MarsRover rover, String outputClass) {
        if (algorithmClass.equals("AlgorithmGreedyLimited")) {
            return new AlgorithmGreedyLimited(rover, outputClass);
        }
        else if (algorithmClass.equals("AlgorithmLimitedScope")) {
            return new AlgorithmLimitedScope(rover, outputClass);
        }
        else if (algorithmClass.equals("AlgorithmLimitedScopeAStar")) {
            return new AlgorithmLimitedScopeAStar(rover, outputClass);
        }
        else if (algorithmClass.equals("AlgorithmGreedyUnlimited")) {
            return new AlgorithmGreedyUnlimited(rover, outputClass);
        }
        else if (algorithmClass.equals("AlgorithmIDAStar")) {
            return new AlgorithmIDAStar(rover, outputClass);
        }
        else if (algorithmClass.equals("AlgorithmUnlimitedBestFirst")) {
            return new AlgorithmUnlimitedBestFirst(rover, outputClass);
        }
        else if (algorithmClass.equals("AlgorithmUnlimitedScopeNonRecursive")) {
            return new AlgorithmUnlimitedScopeNonRecursive(rover, outputClass);
        }
        else if (algorithmClass.equals("AlgorithmUnlimitedScopeRecursive")) {
            return new AlgorithmUnlimitedScopeRecursive(rover, outputClass);
        }
        else  {
            return new Dijkstra(rover, outputClass); //last one has to be in "else" to satisfy Java
        }
        //TODO: add more classes as they are created
    }

}
