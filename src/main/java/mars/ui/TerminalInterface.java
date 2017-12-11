package mars.ui;
import com.sun.corba.se.impl.io.TypeMismatchException;

import mars.coordinate.Coordinate;
import mars.algorithm.AlgorithmLimitedScope;
import mars.algorithm.AlgorithmUnlimitedScope;
import mars.map.GeoTIFF;
import mars.map.TerrainMap;
import mars.rover.MarsRover;

import java.util.Scanner;

/**
 * Class through which users interact with out project.
 * At start up, "promptUser" asks the user for rover specifications
 * (including which algorithm to run, and start/end coordinates).
 * A rover with those specifications is then started and the program runs until completed.
 *
 * TODO properly format with tags -- may not be possible due to how javadoc works
 * slope: maxSlope for input rover
 * startCoords: starting coordinates for input rover
 * endCoords: ending coordinates for input rover
 * mapPath: file path to a terrain map
 * alg: string used to determine what algorithm user chooses
 * fieldOfView:
 */
public class TerminalInterface extends UserInterface {

    double slope = 0;
    Coordinate startCoords;
    Coordinate endCoords;
    String mapPath = "";
    String alg = "";
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

        promptForMap();
        promptForSlope();
        promptForStartCoords();
        promptForEndCoords();
        promptForAlgorithm();
    }

    public void promptForMap() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter the file path for the map you would like to traverse. Example:");
        System.out.println("src/main/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");

        //TODO: Tell people what path their path will be relative to. Or possibly provide options to choose from.
        while(true) {
            try {
                mapPath = scanner.next();
                map.initMap(mapPath);
                break;
            } catch(TypeMismatchException e) {
                System.out.println("Warning: Please enter the file path as a string.");
                scanner.nextLine();
            } catch(Exception e) {
                System.out.println("Warning: Make sure the path you are entering is correct (path is relative to project root).");
                scanner.nextLine();
            }

        }
    }

    public void promptForSlope() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter the maximum slope your rover can handle (in degrees):");
        while(true) {
            try {
                slope = scanner.nextDouble();
                break;
            } catch (Exception e) {
                System.out.println("Warning: Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    public void promptForStartCoords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter start coordinates (pressing enter between each number): ");

        int x;
        int y;
        while(true) {
            try {
                System.out.print("X: ");
                x = scanner.nextInt();
                System.out.print("Y: ");
                y = scanner.nextInt();
                startCoords = new Coordinate(x, y);
                break;
            } catch (Exception e) {
                System.out.println("Warning: Enter coordinates as whole numbers only.");
                scanner.nextLine();
            }
        }
    }

    public void promptForEndCoords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter end coordinates (pressing enter between each number):");

        int x;
        int y;
        while(true) {
            try {
                System.out.print("X: ");
                x = scanner.nextInt();
                System.out.print("Y: ");
                y = scanner.nextInt();
                endCoords = new Coordinate(x, y);
                break;
            } catch (Exception e) {
                System.out.println("Warning: Enter coordinates as whole numbers only.");
                scanner.nextLine();
            }
        }
    }

    public void promptForAlgorithm() {
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
                    try { // get fieldOfView
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

    /**
     * Function to run the requested algorithm with user-prompted variables.
     */
    public void startAlgorithm() {
        //Start Rover then run its algorithm until the output file is populated with results.
        if (alg.equalsIgnoreCase("U")) {
            MarsRover r = new MarsRover(slope, startCoords, endCoords, mapPath);
            algorithm = new AlgorithmUnlimitedScope(map, r);
            algorithm.findPath();
        }
        else if (alg.equalsIgnoreCase("L")) {
            MarsRover r = new MarsRover(slope,startCoords,endCoords,mapPath,fieldOfView);
            algorithm = new AlgorithmLimitedScope(map, r);
            algorithm.findPath();
        }
        else {
            System.out.println("Error: No algorithm selected.");
        }
    }

}

