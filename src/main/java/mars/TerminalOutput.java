package mars;

import java.util.ArrayList;

/**
 * Class which writes a discovered path to the terminal.
 */
public class TerminalOutput extends Output {

    public TerminalOutput(ArrayList<int[]> out) {
        resultList = out;
        writeToOutput();
    }

    public void writeToOutput() {
        System.out.println("\nOutput path: ");
        System.out.println("------------");
        //System.out.println(resultList); //TODO: consider changing this if it only prints the list's address
        for (int i = 1; i <= resultList.size(); i++) {
            int x = resultList.get(i-1)[0];
            int y = resultList.get(i-1)[1];
            System.out.println(i + ". [" + x + ", " + y + "]");
        }
        System.out.println("------------");
    }
}
