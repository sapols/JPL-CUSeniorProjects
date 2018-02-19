package mars.ui;

import mars.algorithm.Algorithm;
import mars.algorithm.AlgorithmFactory;
import mars.algorithm.unlimited.AlgorithmUnlimitedScopeRecursive;
import mars.coordinate.Coordinate;
import mars.algorithm.limited.AlgorithmLimitedScope;
import mars.map.GeoTIFF;
import mars.map.TerrainMap;
import mars.out.*;
import mars.rover.MarsRover;
import java.util.Map;
import java.util.*;
import java.io.*;

/**
 * Class through which users interact with out project.
 * At start up, "promptUser" asks the user for rover specifications
 * (including which algorithm to run, and start/end coordinates).
 * A rover with those specifications is then started and the program runs until completed.
 */
public class TerminalInterface extends UserInterface {
    //TODO: allow command-line arguments

    double slope = 0;
    Coordinate startCoords;
    Coordinate endCoords;
    String mapPath = "";
    String algType = ""; //used to determine limited or unlimited.
    String algorithmClass = ""; //used to instantiate the chosen algorithm by name
    double fieldOfView = 0;
    TerrainMap map = new GeoTIFF();
    String outputClass = "";
    //other variables inherited from "UserInterface"

    /**
     * Function that manually prompts the user for required variables to run. No formal return, input is stored
     * as variables.
     */
    public void promptUser() {
        System.out.println("**==================================================**");
        System.out.println("*  Welcome to the Martian Autonomous Routing System. *");
        System.out.println("**==================================================**\n");
        //TODO: Print basic description of prompts to follow?

        promptForMap();
        promptForSlope();
        promptForStartCoords();
        promptForEndCoords();
        promptForAlgorithm();
        promptForOutput();
        startAlgorithm();
    }

    /**
     * Function to run the requested algorithm with user-prompted variables.
     */
    public void startAlgorithm() {
        MarsRover r;
        if (algType.equalsIgnoreCase("L"))
            r = new MarsRover(slope, startCoords, endCoords, mapPath, fieldOfView);
        else //algType equals "U"
            r = new MarsRover(slope, startCoords, endCoords, mapPath);

        Algorithm algorithm = AlgorithmFactory.getAlgorithm(algorithmClass, r, outputClass);

        try {
            algorithm.findPath();
            OutputFactory.getOutput(algorithm); //Produces output from the completed algorithm
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----Prompting methods---------------------------------------------------------------------------------------------

    /**
     * Prompts the user to select a map from the available tif(f)'s.
     */
    public void promptForMap() {
        System.out.println("Please choose the map you would like to traverse:\n");
        String resourceDir = "src/main/resources/";
        Map<Integer, String> maps = findMaps(resourceDir);

        for (Integer index : maps.keySet()) {
            System.out.println("("+index+") " + maps.get(index));
        }
        System.out.println(); //New line

        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                int mapNum = new Integer(scan.next());
                String mapChoice = maps.get(mapNum);
                if (mapChoice != null) {
                    mapPath = resourceDir + mapChoice;
                    map.initMap(mapPath);
                    break;
                } else {
                    throw new Exception("Please only select from the given options.");
                }
            } catch (Exception ex) {
                System.out.println("Please only select from the given options. Try again:");
            }
        }
    }

    /**
     * Asks the user for the maximum slope that their rover can handle.
     */
    public void promptForSlope() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter the maximum slope your rover can handle (in degrees):");
        while(true) if(checkSlope(scanner)) break;

    }

    public Boolean checkSlope(Scanner scan)
    {
        try {
            slope = scan.nextDouble();
            if(slope >= 0 && slope <= 90) return true;
            else{
                System.out.println("Warning: Slope must be between 0 and 90 degrees.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Warning: Please enter a number between 0 and 90.");
            scan.nextLine();
            return false;
        }
    }

    /**
     * Asks the user for their start coordinates.
     */
    public void promptForStartCoords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter start coordinates (pressing enter between each number): ");

        while(true) if(checkStartCoords(scanner)) break;
    }


    /**
     * Asks the user for their goal coordinates.
     */
    public void promptForEndCoords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter end coordinates (pressing enter between each number):");
        while(true) if(checkEndCoords(scanner))break;

    }

    public Boolean checkStartCoords(Scanner scan) {
        int x;
        int y;
        try {
            System.out.print("X: ");
            x = scan.nextInt();
            System.out.print("Y: ");
            y = scan.nextInt();
            startCoords = new Coordinate(x, y);
            return true;
        } catch (Exception e) {
            System.out.println("Warning: Enter coordinates as whole numbers only.");
            scan.nextLine();
            return false;
        }
    }
    public Boolean checkEndCoords(Scanner scan) {
        int x;
        int y;
        try {
            System.out.print("X: ");
            x = scan.nextInt();
            System.out.print("Y: ");
            y = scan.nextInt();
            endCoords = new Coordinate(x, y);
            return true;
        } catch (Exception e) {
            System.out.println("Warning: Enter coordinates as whole numbers only.");
            scan.nextLine();
            return false;
        }
    }

    /**
     * Asks the user if they want to use an algorithm with an unlimited or limited field of view.
     */
    public void promptForAlgorithmType() {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("\nWould you like to use an algorithm with (U)nlimited scope or (L)imited Scope?");
            algType = scanner.next();
            if(algType.equalsIgnoreCase("U")) {
                break;
            }
            else if(algType.equalsIgnoreCase("L")) {
                scanner.nextLine();
                //TODO: say what the units are here.
                System.out.println("\nEnter the Field of View radius of your rover:");
                while(true) {
                    try {
                        fieldOfView = scanner.nextDouble();
                        break;
                    } catch (Exception e) {
                        System.out.println("Warning: please enter a number for the Field of View radius");
                        scanner.nextLine();
                    }
                }
                break;
            }
            else {
                System.out.println("Warning: Enter 'U' for unlimited scope or 'L' for limited scope");
                scanner.nextLine();
            }
        }
    }

    /**
     * Prompts the user to select an algorithm from the available classes.
     */
    public void promptForAlgorithm() {
        promptForAlgorithmType();

        String algDir = "";
        Map<Integer, String> algorithms;

        //Note: promptForAlgorithmType() will only allow "U" or "L" to get here
        if (algType.equalsIgnoreCase("U"))
            algDir = "src/main/java/mars/algorithm/unlimited/";
        else if (algType.equalsIgnoreCase("L"))
            algDir = "src/main/java/mars/algorithm/limited/";

        algorithms = findAlgorithms(algDir);

        System.out.println("\nPlease choose your algorithm:\n");
        for (Integer index : algorithms.keySet()) {
            System.out.println("("+index+") " + algorithms.get(index));
        }
        System.out.println(); //New line

        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                int algNum = new Integer(scan.next());
                String algChoice = algorithms.get(algNum);
                if (algChoice != null) {
                    algorithmClass = algChoice;
                    break;
                } else {
                    throw new Exception("Please only select from the given options.");
                }
            } catch (Exception ex) {
                System.out.println("Please only select from the given options. Try again:");
            }
        }
    }

    /**
     * Prompts the user to select an output type from the available classes.
     */
    public void promptForOutput() {
        String outputDir = "src/main/java/mars/out/";
        Map<Integer, String> outputs = findOutputs(outputDir);

        System.out.println("\nLast question, please choose your output type:\n");
        for (Integer index : outputs.keySet()) {
            System.out.println("("+index+") " + outputs.get(index));
        }
        System.out.println(); //New line

        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                int outputNum = new Integer(scan.next());
                String outputChoice = outputs.get(outputNum);
                if (outputChoice != null) {
                    outputClass = outputChoice;
                    break;
                } else {
                    throw new Exception("Please only select from the given options.");
                }
            } catch (Exception ex) {
                System.out.println("Please only select from the given options. Try again:");
            }
        }
    }



    //----Resource scanning methods-------------------------------------------------------------------------------------

    /**
     * Returns a Map of [index] -> [name of .tif(f)]
     */
    public Map<Integer, String> findMaps(String dir) {
        File[] files = new File(dir).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".tif") || name.endsWith(".tiff");
            }
        });
        Map<Integer, String> elevationMaps = new HashMap<Integer, String>();

        for (int i = 0; i < files.length; i++) {
            elevationMaps.put(i+1, files[i].getName());
        }

        return elevationMaps;
    }

    /**
     * Returns a Map of [index] -> [name of algorithm]
     */
    public Map<Integer, String> findAlgorithms(String dir) {
        File[] files = new File(dir).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".java");
            }
        });
        Map<Integer, String> algorithms = new HashMap<Integer, String>();

        for (int i = 0; i < files.length; i++) {
            String algorithmName = files[i].getName();
            algorithms.put(i+1, algorithmName.substring(0, algorithmName.indexOf('.'))); //drop ".java"
        }

        return algorithms;
    }

    /**
     * Returns a Map of [index] -> [type of output]
     */
    public Map<Integer, String> findOutputs(String dir) {
        File[] files = new File(dir).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".java");
            }
        });
        Map<Integer, String> outputs = new HashMap<Integer, String>();

        int i = 1;
        for (File file : files) {
            String outputName = file.getName();
            //Don't include the parent class or OutputFactory
            if (!outputName.equalsIgnoreCase("Output.java") && !outputName.equalsIgnoreCase("OutputFactory.java")) {
                outputs.put(i, outputName.substring(0, outputName.indexOf('.'))); //drop ".java"
                i++;
            }

        }

        return outputs;
    }

}
