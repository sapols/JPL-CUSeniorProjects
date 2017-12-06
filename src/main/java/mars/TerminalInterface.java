package mars;
import com.sun.corba.se.impl.io.TypeMismatchException;

import org.omg.IOP.CodecPackage.TypeMismatch;

import java.util.Scanner;

// TODO revisit after wrapper function commit

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
public class TerminalInterface extends UserInterface{
    double slope = 0;
    int[] startCoords = {0, 0};
    int[] endCoords = {0, 0};
    String mapPath = "";
    String alg = "";
    double fieldOfView = 0;
    TerrainMap m = new GeoTIFF();

    //other variables inherited from "UserInterface"

    public void promptUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Martian Autonomous Routing System");
        System.out.println("Please enter the path for the map you would like to traverse");

        while(true) { // get mapPath
            try {
                mapPath = scanner.next();
                m.initMap(mapPath);
                break;
            } catch(TypeMismatchException e) {
                System.out.println("Warning: Please enter a string");
                scanner.nextLine();
            } catch(Exception e) {
                System.out.println("Warning: Make sure the path you are entering is correct");
                scanner.nextLine();
            }

        }
        scanner.nextLine();
        System.out.println("Please enter the max slope your rover can handle");
        while(true) { // get maxSlope
            try {
                slope = scanner.nextDouble();
                break;
            } catch (Exception e) {
                System.out.println("Warning: Please enter a number");
                scanner.nextLine();
            }
        }

        System.out.println("Enter Start Coordinates");
        while(true) { // get startCoords
            try {
                startCoords[0] = scanner.nextInt();
                startCoords[1] = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Warning: Enter coordinates as ints");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        System.out.println("Enter end Coordinates");
        while(true) { // get endCoords
            try {
                endCoords[0] = scanner.nextInt();
                endCoords[1] = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Warning: Enter Coordinates as ints");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        while(true) { // get alg
            System.out.println("Which algorithm would you like to use, (U)nlimited scope or (L)imited Scope");
            alg = scanner.next(); //TODO suggest switch statement to implement this
            if(alg.equals("U") || alg.equals("u") ) {
                startAlgorithm();
                break;
            }
            else if(alg.equals("L") || alg.equals("l")) {
                scanner.nextLine();
                System.out.println("Enter the Field of View Radius of your rover");
                while(true) { // get fieldOfView
                    try {
                        fieldOfView = scanner.nextDouble();
                        break;
                    } catch (Exception e) {
                        System.out.println("Warning: please enter double for Field of View Radius");
                        scanner.nextLine();
                    }
                }
                startAlgorithm();
                break;
            }
            else {
                System.out.println("Warning: Enter U for Unlimited Scope or L for limited scope");
                scanner.nextLine();
            }
        }

    }

    public void startAlgorithm() {
        //Start Rover then run its algorithm until the output file is populated with results.
        if (alg.equals("U") || alg.equals("u")) {
            MarsRover r = new MarsRover(slope, startCoords, endCoords, mapPath);
            algorithm = new OptimalAlgorithm(m, r);
            algorithm.findPath();
        }
        else if (alg.equals("L") || alg.equals("l")) {
            MarsRover r = new MarsRover(slope,startCoords,endCoords,mapPath,fieldOfView);
            algorithm = new SuboptimalAlgorithm(m, r);
            algorithm.findPath();
        }
        else {
            System.out.println("Error: No algorithm Selected");
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

