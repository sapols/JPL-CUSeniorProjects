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
        if (algorithmClass.equals("AlgorithmUnlimitedScopeRecursive")) {
            return new AlgorithmUnlimitedScopeRecursive(rover, outputClass);
        }
        else if (algorithmClass.equals("AlgorithmUnlimitedScopeNonRecursive")) {
            return new AlgorithmUnlimitedScopeNonRecursive(rover, outputClass);
        }
        else if (algorithmClass.equals("Dijkstra")) {
            return new Dijkstra(rover, outputClass);
        }
//        else if (algorithmClass.equals("AlgorithmGreedy")) {
//            return new AlgorithmGreedy(outputClass, rover); //NOTE: this is getting String then rover because of "_mode"
//        }
        else {
            return new AlgorithmLimitedScope(rover, outputClass);
        }
        //TODO: add more classes as they are created
    }

}
