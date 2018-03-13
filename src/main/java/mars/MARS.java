package mars;

import mars.algorithm.AlgorithmFactory;
import mars.algorithm.unlimited.*;
import mars.algorithm.limited.*;
import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.out.OutputFactory;
import mars.rover.MarsRover;
import mars.ui.TerminalInterface;

import java.io.File;
import java.io.PrintWriter;
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
    public static void main(String[] args) throws Exception{
        Coordinate startCoord = new Coordinate(880,950);
        Coordinate endCoord = new Coordinate(980,500);
        String mapPath = "src/main/resources/mi15S158E.tif";
        ArrayList<String> algs = new ArrayList<String>();

        MarsRover rover = new MarsRover(10,"P",startCoord,endCoord,mapPath,10);
        Algorithm alg = new UnlimitedAStarRecursive(rover,"MapImageOutput");

        alg.findPath();
        OutputFactory.getOutput(alg);


        /*algs.add("LimitedGreedy");
        algs.add("LimitedAStar");
        algs.add("LimitedBestFirst");
        algs.add("LimitedBreadthFirstSearch");
        algs.add("LimitedDijkstra");
        algs.add("UnlimitedAStarNonRecursive");
        algs.add("UnlimitedAStarRecursive");
        algs.add("UnlimitedBestFirst");
        algs.add("UnlimitedBreadthFirstSearch");
        algs.add("UnlimitedDijkstra");
        algs.add("UnlimitedGreedy");

        doEval(algs, startCoord, endCoord, mapPath);
        */

        //TerminalInterface ti = new TerminalInterface();
        //ti.promptUser();
    }

    public static ArrayList<Integer> eval(String algorithm, MarsRover newRover) throws Exception{
        long startTime, endTime, newTime;
        int newLength;
        ArrayList<Long> times = new ArrayList<Long>();
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        for(int i = 1; i < 11; i++){
            Algorithm newAlgorithm = AlgorithmFactory.getAlgorithm(algorithm,newRover,"TerminalOutput");
            startTime = System.nanoTime();
            newAlgorithm.findPath();
            endTime = System.nanoTime();
            newTime = endTime - startTime;
            newLength = (newAlgorithm.getPath()).size();
            times.add(newTime);
            lengths.add(newLength);
            System.out.println("Run " + i + "/10 complete: " + newLength + " steps in " + (newTime / 1000000) + " ms");
        }
        long timeTotal = 0;
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
        results.add((int)timeTotal);
        return results;
    }

    public static void doEval(ArrayList<String> algorithms, Coordinate startCoord, Coordinate endCoord, String mapPath) throws Exception{
        String outFile = "algorithm,";
        ArrayList<Integer> outResults;
        int algCount = algorithms.size();
        int testsDone = 1;
        int testsTotal = 0;
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 4; j++){
                testsTotal += algCount;
                outFile += "steps slope " + (7 + i*2) + " fov " + (10 + j*10) + ",time slope " + (7 + i*2) + " fov " + (10 + j*10) + ",";
            }
        }
        System.out.println("Header written");
        MarsRover rover = new MarsRover(0,"P",startCoord,endCoord,mapPath,0);
        System.out.println("Rover instantiated");
        for(String algorithmName : algorithms){
            outFile += "\n" + algorithmName + ",";
            for(int i = 0; i < 13; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.println("Testing alg " + algorithmName + " with slope " + (7 + i*2) + " fov " + (10 + j*10) + " (Test " + testsDone + "/" + testsTotal + ")");
                    testsDone += 1;
                    rover.setMaxSlope(7+i*2);
                    rover.setFieldOfView(10+j*10);
                    outResults = eval(algorithmName, rover);
                    outFile += outResults.get(0) + "," + outResults.get(1) + ",";
                }
            }
        }
        PrintWriter pw = new PrintWriter(new File("results.csv"));
        pw.write(outFile);
        pw.close();
    }
}
