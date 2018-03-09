package mars;

import mars.algorithm.AlgorithmFactory;
import mars.algorithm.unlimited.*;
import mars.algorithm.limited.*;
import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.out.OutputFactory;
import mars.rover.MarsRover;
import mars.ui.TerminalInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * This class houses the main method which starts our program.
 */
public class MARS {

    /**
     * Main method to be called to start the program.
     * @param args unused
     */
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        Coordinate startCoord = new Coordinate(7568,1507);
        Coordinate endCoord = new Coordinate(7568,1727);
        String mapPath = "src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif";
        MarsRover rover = new MarsRover(8,"P",startCoord,endCoord,mapPath,40);
        Algorithm algorithm = new LimitedBreadthFirstSearch(rover,"MapImageOutput,TerminalOutput");
        try{
            algorithm.findPath();
        } catch (Exception expectedException) {
            //
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println(duration/1000000);

        OutputFactory.getOutput(algorithm);



        //TerminalInterface ti = new TerminalInterface();
        //ti.promptUser();
    }

    public static ArrayList<Integer> eval(String algorithm, int slope, Coordinate startCoord, Coordinate endCoord, String mapPath, int fov) throws Exception{
        long startTime, endTime;
        ArrayList<Long> times = new ArrayList<Long>();
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        MarsRover newRover = new MarsRover(slope,"P", startCoord, endCoord, mapPath, fov);
        for(int i = 0; i < 10; i++){
            Algorithm newAlgorithm = AlgorithmFactory.getAlgorithm(algorithm,newRover,"TerminalOutput");
            startTime = System.nanoTime();
            newAlgorithm.findPath();
            endTime = System.nanoTime();
            times.add((endTime - startTime));
            lengths.add((newAlgorithm.getPath()).size());
            System.out.println("Run " + i + "/10");
        }
        int timeTotal = 0;
        for(long time : times){
            timeTotal += time;
        }
        timeTotal = timeTotal / 10000000; //divide by 1000000, then another 10 for the number of iterations
        int lengthsTotal = 0;
        for(int length : lengths){
            lengthsTotal += length;
        }
        lengthsTotal = lengthsTotal / 10;
        ArrayList<Integer> results = new ArrayList<Integer>();
        results.add(lengthsTotal);
        results.add(timeTotal);
        return results;
    }

    public void doEval(ArrayList<String> algorithms, Coordinate startCoord, Coordinate endCoord, String mapPath) throws Exception{
        String outFile = "algorithm,type,";
        ArrayList<Integer> outResults;
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 3; j++){
                outFile += "steps slope " + (6 + i*2) + " fov " + (10 + j*10) + ",time (milli) slope " + (6 + i*2) + " fov " + (10 + j*10);
            }
        }
        for(String algorithmName : algorithms){
            outFile += "\n" + algorithmName + ",";
            for(int i = 0; i < 13; i++) {
                for (int j = 0; j < 3; j++) {
                    outResults = eval(algorithmName, (6 + i*2), startCoord, endCoord, mapPath, (10 + j*10));
                    outFile += outResults.get(0) + "," + outResults.get(1);
                }
            }
        }
        
    }
}
