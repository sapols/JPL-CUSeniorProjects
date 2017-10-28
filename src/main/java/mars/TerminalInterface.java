package mars;

/**
 * Class through which users interact with out project.
 * At start up, "promptUser" asks the user for rover specifications
 * (including a flag for which algorithm to run, and start/end coordinates).
 * A rover with those specifications is then started and the program run until completed.
 */
public class TerminalInterface {

    double slope = 0;
    int[] startCoords = {0, 0};
    int[] endCoords = {0, 0};
    String mapPath = "";

    public void promptUser() {
        //Get user input
    }

    public void startRover() {
        //Start Rover then run its algorithm until the output file is populated with results.
    }

}
