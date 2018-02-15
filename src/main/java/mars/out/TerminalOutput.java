package mars.out;

import jj2000.j2k.codestream.CoordInfo;
import mars.coordinate.*;
import mars.map.GeoTIFF;

import java.util.*;

/**
 * Class which writes a discovered path to the terminal.
 */
public class TerminalOutput extends Output {

    GeoTIFF convert = new GeoTIFF();
    /**
     * Constructor for TerminalOutput.
     * It immediately prints the output.
     * @param out list of coordinates generated by program
     */
    public TerminalOutput(List<? extends Coordinate> out) {
        List<? extends Coordinate> newList = new ArrayList<Coordinate>();
        resultList = out;
        writeToOutput();
    }

    /**
     * Function that outputs resultList to terminal in a user-friendly format.
     */
    public void writeToOutput() {
        System.out.println("\nOutput path: ");
        System.out.println("------------");
        for (int i = 1; i <= resultList.size(); i++) {
            int x = resultList.get(i-1).getX();
            int y = resultList.get(i-1).getY();

            Coordinate latLongCoord = convert.coordinate2LatLong(new Coordinate(x,y));
            //latLongCoord.getX()
            //latLongCoord.getY()

            System.out.println(i + ". (" + latLongCoord.getX() + ", " + latLongCoord.getY() + ")");
        }
        System.out.println("------------");
    }
}
