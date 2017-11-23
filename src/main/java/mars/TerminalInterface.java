package mars;

/**
 * Class through which users interact with out project.
 * At start up, "promptUser" asks the user for rover specifications
 * (including which algorithm to run, and start/end coordinates).
 * A rover with those specifications is then started and the program runs until completed.
 */
public class TerminalInterface extends UserInterface{

    double slope = 0;
    int[] startCoords = {0, 0};
    int[] endCoords = {0, 0};
    String mapPath = "";
    boolean limitedScope = false; //TODO: consider how to decide which algorithm
    //other variables inherited from "UserInterface"

    public void promptUser() {
        //Get user input
    }

    public void startAlgorithm() {
        //Start Rover then run its algorithm until the output file is populated with results.
    }

}
