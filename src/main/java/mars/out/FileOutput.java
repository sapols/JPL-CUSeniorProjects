package mars.out;

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

    public FileOutput(List<? extends Coordinate> out) throws IOException {
        resultList = out;
        writer = new FileWriter(new File("MARS_output.csv"));
        writeToOutput();
    }

    public void writeToOutput() throws IOException {
        //Write resultList to a file
        for (int i = 1; i <= resultList.size(); i++) {
            int x = resultList.get(i-1).getX();
            int y = resultList.get(i-1).getY();
            writer.append(Integer.toString(x) + ',' + Integer.toString(y) +'\n');
        }
        writer.close();
    }

}
