package mars.map;

import com.sun.corba.se.impl.io.TypeMismatchException;
import mars.coordinate.Coordinate;
import java.util.Scanner;

/**
 * A class which allows us (the developers) to see the elevation values
 * of pixels in a given area from a specified elevation map.
 */
public class MapElevationsViewer  {

    Coordinate originCoords;
    int width;
    int height;
    String mapPath = "";
    GeoTIFF map = new GeoTIFF();
    double[][] elevations;

    /**
     * Function that manually prompts the user for required variables to run. No formal return, input is stored
     * as variables.
     */
    public void promptUser() {
        System.out.println("**--------------------------------------------------**");
        System.out.println("*        Welcome to the Map Elevations Viewer.       *");
        System.out.println("**--------------------------------------------------**");
        System.out.println("This simple tool prints out the elevations of pixels\n" +
                "within a given rectangular area, defined by an origin\n" +
                "point and a width and height from the origin\n" +
                "(width and height cannot be negative).");

        promptForMap();
        promptForOriginCoords();
        promptForWidthAndHeight();
        printElevations();

    }

    public void promptForMap() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter the file path for the map you'd like to inspect. Example:");
        System.out.println("src/main/resources/Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif");

        //TODO: Tell people what path their path will be relative to. Or possibly provide options to choose from.
        while(true) if(checkMap(scanner)) break;


    }

    public Boolean checkMap(Scanner scan) {
        try {
            mapPath = scan.next();
            map.initMap(mapPath);
            return true;
        } catch(TypeMismatchException e) {
            System.out.println("Warning: Please enter the file path as a string.");
            scan.nextLine();
            return false;

        } catch(Exception e) {
            System.out.println("Warning: Make sure the path you are entering is correct (path is relative to project root).");
            scan.nextLine();
            return false;
        }
    }


    public void promptForOriginCoords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter origin coordinates (pressing enter between each number): ");

        while(true) if(checkOriginCoords(scanner)) break;
    }

    public Boolean checkOriginCoords(Scanner scan) {
        int x;
        int y;
        try {
            System.out.print("X: ");
            x = scan.nextInt();
            System.out.print("Y: ");
            y = scan.nextInt();
            originCoords = new Coordinate(x, y);
            return true;
        } catch (Exception e) {
            System.out.println("Warning: Enter coordinates as whole numbers only.");
            scan.nextLine();
            return false;
        }
    }

    public void promptForWidthAndHeight() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the width and height of the area you'd like to inspect (pressing enter between each number): ");

        try {
            System.out.print("Width: ");
            width = scanner.nextInt();
            System.out.print("Height: ");
            height = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Warning: Enter width/height as whole numbers only.");
            scanner.nextLine();
        }
    }

    /**
     * Print out the elevations to the console, formatted as a x-y chart
     */
    public void printElevations() {
        System.out.println("\nELEVATIONS:");
        elevations = map.getElevationsInArea(originCoords, width, height);
        double elevation = 0.0;
        int yNum = originCoords.getY() + height-1;
        String yAxis = "";
        int x = originCoords.getX();

        try {
            for (int i = 0; i < height; i++) {
                //Print the y-axis labels before each row
                System.out.println();
                yAxis = "" + yNum + " | ";
                System.out.print(yAxis);
                yNum--;

                for (int j = 0; j < width; j++) {
                    elevation = elevations[i][j];
                    System.out.print("" + elevation + getPaddingSpaces(Double.toString(elevation), 6));
                }
            }
            //Print x-axis labels below the elevations
            printXAxis(yAxis, elevation, x);
        } catch (Exception e) {
            System.out.println("Something went wrong..."); //Improve this message?
        }
    }

    /**
     * A simple helper method which uses padding spaces to improve formatting
     */
    public String getPaddingSpaces(String elevation, int cellWidth) {
        String spaces = "";

        for (int i = 0; i < (cellWidth-elevation.length()); i++) {
            spaces = spaces + " ";
        }

        return spaces;
    }

    /**
     * Prints x-axis labels below the elevations
     */
    public void printXAxis(String yAxis, double elevation, int x) {
        System.out.print("\n" + getPaddingSpaces(yAxis, 3+yAxis.length()) + "+");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < Double.toString(elevation).length() + 2; j++) {
                System.out.print("-");
            }
        }
        System.out.print("\n" + getPaddingSpaces(yAxis, 3+yAxis.length()) + "  ");
        for (int i = 0; i < width; i++) {
            System.out.print(x);
            x++;
            for (int j = 0; j < Double.toString(elevation).length(); j++) {
                System.out.print(" ");
            }
        }
    }
    
}

