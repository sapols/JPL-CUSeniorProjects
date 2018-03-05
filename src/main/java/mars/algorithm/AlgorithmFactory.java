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
        if (algorithmClass.equals("LimitedGreedy")) {
            return new LimitedGreedy(rover, outputClass);
        }
        else if (algorithmClass.equals("LimitedAStar")) {
            return new LimitedAStar(rover, outputClass);
        }
        else if (algorithmClass.equals("UnlimitedGreedy")) {
            return new UnlimitedGreedy(rover, outputClass);
        }
        else if (algorithmClass.equals("UnlimitedIDAStar")) {
            return new UnlimitedIDAStar(rover, outputClass);
        }
        else if (algorithmClass.equals("UnlimitedBestFirst")) {
            return new UnlimitedBestFirst(rover, outputClass);
        }
        else if (algorithmClass.equals("UnlimitedAStarNonRecursive")) {
            return new UnlimitedAStarNonRecursive(rover, outputClass);
        }
        else if (algorithmClass.equals("UnlimitedAStarRecursive")) {
            return new UnlimitedAStarRecursive(rover, outputClass);
        }
        else if (algorithmClass.equals("UnlimitedBreadthFirstSearch")){
            return new UnlimitedBreadthFirstSearch(rover, outputClass);
        }
        else if (algorithmClass.equals("LimitedBreadthFirstSearch")){
            return new LimitedBreadthFirstSearch(rover, outputClass);
        }
        else if (algorithmClass.equals("LimitedDijkstra")){
            return new LimitedDijkstra(rover, outputClass);
        }
        else if (algorithmClass.equals("LimitedIDAStar")){
            return new LimitedIDAStar(rover, outputClass);
        }
        else if (algorithmClass.equals("LimitedBestFirst")){
            return new LimitedBestFirst(rover, outputClass);
        }
        else  {
            return new UnlimitedDijkstra(rover, outputClass); //last one has to be in "else" to satisfy Java
        }
        //TODO: add more classes as they are created
    }

}
