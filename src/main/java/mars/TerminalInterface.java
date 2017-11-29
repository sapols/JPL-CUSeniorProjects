package mars;

import java.util.Scanner;

/**
 * Class through which users interact with out project.
 * At start up, "promptUser" asks the user for rover specifications
 * (including which algorithm to run, and start/end coordinates).
 * A rover with those specifications is then started and the program runs until completed.
 */
public class TerminalInterface extends UserInterface{

    double slope = 0;
    int[] startCoords = {0, 0};
    int[] endCoords = {0, 0};
    String mapPath = "";
    String alg = ""; //used to determine Algorithm to use
    double fieldOfView = 0;

    //other variables inherited from "UserInterface"

    public void promptUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Martian Autonomous Routing System");
        System.out.println("Please enter the path for the map you would like to traverse");
        while(true)
        {
            try{
                mapPath = scanner.next();
                break;
            }catch(Exception e)
            {
                System.out.println("Warning: Please enter a string");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        System.out.println("Please enter the max slope your rover can handle");
        while(true) {
            try {

                slope = scanner.nextDouble();
                break;
            } catch (Exception e) {

                System.out.println("Warning: Please enter a number");
                scanner.nextLine();
            }
        }

        System.out.println("Enter Start Coordinates");
        while(true) {
            try {
                startCoords[0] = scanner.nextInt();
                startCoords[1] = scanner.nextInt();
                break;
            }
            catch (Exception e){
                System.out.println("Warning: Enter coordinates as ints");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        System.out.println("Enter end Coordinates");
        while(true) {
            try {
                endCoords[0] = scanner.nextInt();
                endCoords[1] = scanner.nextInt();
                break;
            } catch (Exception e){
                System.out.println("Warning: Enter Coordinates as ints");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        while(true) {
            System.out.println("Which algorithm would you like to use, (U)nlimited scope or (L)imited Scope");
            alg = scanner.next();
            if(alg.equals("U") || alg.equals("u") )
            {
                startAlgorithm();
                break;
            }
            else if(alg.equals("L") || alg.equals("l"))
            {
                scanner.nextLine();
                System.out.println("Enter the Field of View Radius of your rover");
                while(true) {
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
            else
            {
                System.out.println("Warning: Enter U for Unlimited Scope or L for limited scope");
                scanner.nextLine();
            }
        }

    }

    public void startAlgorithm() {
        //Start Rover then run its algorithm until the output file is populated with results.
        if (alg.equals("U") || alg.equals("u"))
        {
            MarsRover r = new MarsRover(slope, startCoords, endCoords, mapPath);
            //OptimalAlgorithm.OptimalAlgorithm( map, r);
        }
        else if (alg.equals("L") || alg.equals("l"))
        {
            MarsRover r = new MarsRover(slope,startCoords,endCoords,mapPath,fieldOfView);
            //SuboptimalAlgorithm(r, mapPath);
        }
        else
        {
            System.out.println("Error: No algorithm Selected");
        }
    }
}

