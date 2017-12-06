package mars;
import com.sun.corba.se.impl.io.TypeMismatchException;

import org.omg.IOP.CodecPackage.TypeMismatch;

import java.util.Scanner;

/**
 * Class through which users interact with out project.
 * At start up, "promptUser" asks the user for rover specifications
 * (including which algorithm to run, and start/end coordinates).
 * A rover with those specifications is then started and the program runs until completed.
 */
public class TerminalInterface extends UserInterface {

    double slope = 0;
    int[] startCoords = {0, 0};
    int[] endCoords = {0, 0};
    String mapPath = "";
    String alg = ""; //used to determine Algorithm to use. TODO: make this a local param instead of global?
    double fieldOfView = 0;
    TerrainMap map = new GeoTIFF();

    //other variables inherited from "UserInterface"

    public void promptUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Martian Autonomous Routing System.\n");
        System.out.println("Please enter the file path for the map you would like to traverse. Example:");
        System.out.println("src/main/resources/Phobos_ME_HRSC_DEM_Global_2ppd.tiff");

        //Prompt for the map path
        //TODO: refactor into "promptForMap() method.
        //TODO: tell people what path their path will be relative to. Possibly provide options to choose from.
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

        //Prompt for the rover's slope
        //TODO: refactor into "promptForSlope()" method
        scanner.nextLine();
        System.out.println("Please enter the maximum slope your rover can handle (in degrees):");
        while(true) {
            try {
                slope = scanner.nextDouble();
                break;
            } catch (Exception e) {
                System.out.println("Warning: Please enter a number.");
                scanner.nextLine();
            }
        }

        //Prompt for the start coordinates
        //TODO: refactor into "promptForStartCoords()" method
        //TODO: use newly created "Coordinate" class
        //TODO: make input format (including X vs. Y) more clear.
        System.out.println("Enter start coordinates (pressing enter between each number): ");
        while(true) {
            try {
                System.out.print("X: ");
                startCoords[0] = scanner.nextInt();
                System.out.print("Y: ");
                startCoords[1] = scanner.nextInt();
                break;
            }
            catch (Exception e) {
                System.out.println("Warning: Enter coordinates as whole numbers only."); //TODO: handle this in Coordinate
                scanner.nextLine();
            }
        }

        //Prompt for the end coordinates
        //TODO: refactor into "promptForEndCoords()" method
        //TODO: use newly created "Coordinate" class
        scanner.nextLine();
        System.out.println("Enter end coordinates (pressing enter between each number):");
        while(true) {
            try {
                System.out.print("X: ");
                endCoords[0] = scanner.nextInt();
                System.out.print("Y: ");
                endCoords[1] = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Warning: Enter cordinates as whole numbers only."); //TODO: handle this in Coordinate
                scanner.nextLine();
            }
        }

        //Prompt for which algorithm to use
        //TODO: refactor into "promptForAlgorithm()" method
        scanner.nextLine();
        while(true) {
            System.out.println("Which algorithm would you like to use? (U)nlimited scope or (L)imited Scope:");
            alg = scanner.next();
            if(alg.equalsIgnoreCase("U")) {
                startAlgorithm();
                break;
            }
            else if(alg.equalsIgnoreCase("L")) {
                scanner.nextLine();
                //TODO: say what the units are here.
                System.out.println("Enter the Field of View radius of your rover:");
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


    /*  For Testing ***
    public static void main(String[] args)
    {
        TerminalInterface ti = new TerminalInterface();
        ti.promptUser();
    }
    */

}

