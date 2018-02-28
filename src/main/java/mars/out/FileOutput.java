package mars.out;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Class which writes a discovered path to an output file
 */
public class FileOutput extends Output {

    private FileWriter writer;

    /**
     * Constructor for CSV file output which takes an algorithm.
     * It pulls the discovered path from the algorithm then immediately writes the file.
     * @param algorithm The completed algorithm which stores the discovered path to be output.
     * @throws IOException
     */
    public FileOutput(Algorithm algorithm) throws IOException {
        resultList = algorithm.getPath();
        coordinateType = algorithm.rover.getCoordType();
        writer = new FileWriter(new File("MARS_output.csv"));
        writeToOutput();
    }

    /**
     * Constructor for CSV file output which takes a list of Coordinates.
     * It immediately writes the file.
     *
     * @param out The list of Coordinates to be output
     * @throws IOException
     */
    public FileOutput(List<? extends Coordinate> out) throws IOException {
        resultList = out;
        writer = new FileWriter(new File("MARS_output.csv"));
        writeToOutput();
    }

    public void writeToOutput() throws IOException {
        writer.append("x,y\n");
        //Write resultList to a file
        try {
            for (int i = 1; i <= resultList.size(); i++) {
                int x = resultList.get(i - 1).getX();
                int y = resultList.get(i - 1).getY();
                writer.append(Integer.toString(x) + ',' + Integer.toString(y) + '\n');
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        writer.close();
    }

}
