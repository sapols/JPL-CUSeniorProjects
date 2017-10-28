package mars;

/**
 * Interface which the TerminalInterface class implements
 */
public interface ITerminalInterface {

    double slope = 0;
    int[] startCoords = {0, 0};
    int[] endCoords = {0, 0};
    String mapPath = "";

    public void promptUser();

    public void startRover();

}
