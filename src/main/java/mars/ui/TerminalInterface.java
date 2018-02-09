package mars.ui;

import com.sun.corba.se.impl.io.TypeMismatchException;
import mars.coordinate.Coordinate;
import mars.algorithm.AlgorithmLimitedScope;
import mars.algorithm.*;
import mars.map.GeoTIFF;
import mars.map.TerrainMap;
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
    String alg = ""; //used to determine Algorithm to use.
    double fieldOfView = 0;
    TerrainMap map = new GeoTIFF();
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
    }

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

//    public Boolean checkMap(Scanner scan)
//    {
//        try {
//            int mapChoice = scan.nextInt();
//            mapPath = scan.next(
//            map.initMap(mapPath);
//            return true;
//        } catch(TypeMismatchException e) {
//            System.out.println("Warning: Please enter the file path as a string.");
//            scan.nextLine();
//            return false;
//
//        } catch(Exception e) {
//            System.out.println("Warning: Make sure the path you are entering is correct (path is relative to project root).");
//            scan.nextLine();
//            return false;
//        }
//    }

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

    public void promptForStartCoords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter start coordinates (pressing enter between each number): ");

        while(true) if(checkStartCoords(scanner)) break;
    }


    public void promptForEndCoords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter end coordinates (pressing enter between each number):");
        while(true) if(checkEndCoords(scanner))break;

    }

    public Boolean checkEndCoords(Scanner scan)
    {   int x;
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
    public Boolean checkStartCoords(Scanner scan)
    {   int x;
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

    public void promptForAlgorithm() {
        //TODO: Use AlgorithmFactory
        //TODO: Call to method which returns a map of (index) -> (name of .java in src/main/java/mars/algorithm)
        //TODO: Consider Unlimited/Limited (return different options depending (use unlimited/limited subdirectories))
        //TODO: List options in map
        //TODO: Call to AlgorithmFactory to get Algorithm from name
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("\nWhich algorithm would you like to use? (U)nlimited scope or (L)imited Scope:");
            alg = scanner.next();
            if(alg.equalsIgnoreCase("U")) {
                startAlgorithm();
                break;
            }
            else if(alg.equalsIgnoreCase("L")) {
                scanner.nextLine();
                //TODO: say what the units are here.
                System.out.println("\nEnter the Field of View radius of your rover:");
                while(true) {
                    try {
                        fieldOfView = scanner.nextDouble();
                        break;
                    } catch (Exception e) {
                        System.out.println("Warning: please enter number for the Field of View Radius");
                        scanner.nextLine();
                    }
                }
                startAlgorithm();
                break;
            }
            else {
                System.out.println("Warning: Enter 'U' for Unlimited Scope or 'L' for limited scope");
                scanner.nextLine();
            }
        }
    }

    //TODO: Prompt for ouput type!

    /**
     * Function to run the requested algorithm with user-prompted variables.
     */
    public void startAlgorithm() {
        //TODO: Give algorithm straight from Algorithm object return from ALgorithmFactory.
        //Start Rover then run its algorithm until the output file is populated with results.
        if (alg.equalsIgnoreCase("U")) {
            MarsRover r = new MarsRover(slope, startCoords, endCoords, mapPath);
            algorithm = new AlgorithmUnlimitedScopeRecursive(r);

            try {
                algorithm.findPath();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else if (alg.equalsIgnoreCase("L")) {
            MarsRover r = new MarsRover(slope, startCoords, endCoords, mapPath, fieldOfView);
            algorithm = new AlgorithmLimitedScope(r);

            try {
                algorithm.findPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Error: No algorithm selected.");
        }
    }

    //----Resource scanning methods-------------------------------------------------------------------------------------

    /**
     * Returns a Map of [index] -> [name of .tif(f) in src/main/resources/]
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

}
