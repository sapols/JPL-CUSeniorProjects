package mars;

import mars.ui.TerminalInterface;

/**
 * This class houses the main method which starts our program.
 */
public class MARS {

    public static void main(String[] args) {
        TerminalInterface ti = new TerminalInterface();
        ti.promptUser();
    }

}
