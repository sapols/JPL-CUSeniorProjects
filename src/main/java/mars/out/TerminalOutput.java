package mars.out;

import mars.coordinate.Coordinate;

import java.util.ArrayList;

/**
 * Class which writes a discovered path to the terminal.
 */
public class TerminalOutput extends Output {

    /*
     * Constructor for TerminalOutput.
     * It immediately prints the output.
     */
    public TerminalOutput(ArrayList<Coordinate> out) {
        resultList = out;
        writeToOutput();
    }

    public void writeToOutput() {
        System.out.println("\nOutput path: ");
        System.out.println("------------");
        for (int i = 1; i <= resultList.size(); i++) {
            int x = resultList.get(i-1).getX();
            int y = resultList.get(i-1).getY();
            System.out.println(i + ". (" + x + ", " + y + ")");
        }
        System.out.println("------------");
    }
}
