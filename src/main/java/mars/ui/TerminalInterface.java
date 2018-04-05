package mars.ui;

import mars.algorithm.Algorithm;
import mars.algorithm.AlgorithmFactory;
import mars.coordinate.Coordinate;
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

    public double slope = 0;
    public Coordinate startCoords;
    public Coordinate endCoords;
    public String coordType = "";
    public String mapPath = "";
    public String algType = ""; //used to determine limited or unlimited.
    public String algorithmClass = ""; //used to instantiate the chosen algorithm by name
    public double fieldOfView = 0;
    TerrainMap map = new GeoTIFF();
    public String outputClass = "";
    String latLong = "";

    //other variables inherited from "UserInterface"

    /**
     * Function that manually prompts the user for required variables to run. No formal return, input is stored
     * as variables.
     */
    public void promptUser() {
        System.out.println("**==================================================**");
        System.out.println("*  Welcome to the Martian Autonomous Routing System. *");
        System.out.println("**==================================================**\n");

        if( mapPath.compareTo("") == 0) promptForMap();
        if( slope == 0 ) promptForSlope();
        promptForLatLong();
        if( startCoords == null)  promptForStartCoords();
        if( endCoords == null) promptForEndCoords();
        if( algorithmClass.compareTo("") == 0) promptForAlgorithm();
        if( coordType.compareTo("") == 0) promptForCoordOutput();
        if( outputClass.compareTo("") == 0) promptForOutput();

        startAlgorithm();
    }

    /**
     * Function to run the requested algorithm with user-prompted variables.
     */
    public void startAlgorithm() {
        MarsRover r;
        if (fieldOfView != 0)
            r = new MarsRover(slope, coordType, startCoords, endCoords, mapPath, fieldOfView);
        else //algType equals "U"
            r = new MarsRover(slope, coordType ,startCoords, endCoords, mapPath);

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
     * Asks the user to specify the type of input coordingates they want to use.
     */
    public void promptForLatLong(){
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("\nWould you like to input your coordinates in (P) pixels or (L) latlong?");
            latLong = scanner.next();
            if(latLong.equalsIgnoreCase("P")){
                break;
            }
            else if(latLong.equalsIgnoreCase("L")){
                break;
            }
            else{
                System.out.println("Warning: Enter 'P' for pixels or 'L' for latitude and longitude");
                scanner.nextLine();
            }
        }

    }
    /**
     * Asks the user for their start coordinates.
     */
    public void promptForStartCoords() {
        Scanner scanner = new Scanner(System.in);

        if(latLong.equalsIgnoreCase("L")){
            System.out.println("\nEnter start coordinates in Lat/Long (pressing enter between each number): ");
        }
        else{
            System.out.println("\nEnter start coordinates in Pixels (pressing enter between each number): ");
        }
        while(true) if(checkStartCoords(scanner)) break;
    }

    /**
     * Asks the user for their goal coordinates.
     */
    public void promptForEndCoords() {
        Scanner scanner = new Scanner(System.in);
        if(latLong.equalsIgnoreCase("L")){
            System.out.println("\nEnter goal coordinates in Lat/Long (pressing enter between each number): ");
        }
        else{
            System.out.println("\nEnter goal coordinates in Pixels (pressing enter between each number): ");
        }
        while(true) if(checkEndCoords(scanner))break;
    }

    public Boolean checkStartCoords(Scanner scan) {
        if(latLong.equalsIgnoreCase("L")){
            double x;
            double y;

            double leftbound = 135;
            double pixeldiff = 7680;

            double pixelx = 0;
            double pixely = 0;

            try{
                System.out.print("Lat: ");
                y = scan.nextDouble();
                System.out.print("Long: ");
                x = scan.nextDouble();

                if((x>=135 && x<=180) && (y>=-30 && y<=0)){
                    //used to calculate map section displacement
                    double Diff = x - leftbound;
                    pixelx = Diff * 256.0;

                    pixely = y * -256.0;
                    pixely = pixeldiff - pixely;

                    System.out.println("Longitude: "+(int)pixelx);
                    System.out.println("Latitude: "+(int)pixely);

                    startCoords = new Coordinate((int)pixelx, (int)pixely);
                    return true;
                }
                else{
                    System.out.println("Warning map degrees are out of bounds or not entered as decimals.\n" +
                            "Please ensure that Latitude is between 135 and 180 and Longitude is between 0 and -30");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Warning: Enter coordinates as decimal values only");
                scan.nextLine();
                return false;
            }
        }
        else{
            int x;
            int y;
            try {
                System.out.print("X: ");
                x = scan.nextInt();
                System.out.print("Y: ");
                y = scan.nextInt();
                System.out.println("Latitude: " + y);
                System.out.println("Longitude: " + x);
                startCoords = new Coordinate(x, y);
                return true;
            } catch (Exception e) {
                System.out.println("Warning: Enter coordinates as whole numbers only.");
                scan.nextLine();
                return false;
            }

        }
    }
    public Boolean checkEndCoords(Scanner scan) {
        if(latLong.equalsIgnoreCase("L")){
            double x;
            double y;

            double leftbound = 135;
            double pixeldiff = 7680;

            double pixelx = 0;
            double pixely = 0;

            try{
                System.out.print("Lat: ");
                y = scan.nextDouble();
                System.out.print("Long: ");
                x = scan.nextDouble();

                if((x>=135 && x<=180) && (y>=-30 && y<=0)){
                    //used to calculate map section displacement
                    double Diff = x - leftbound;
                    pixelx = Diff * 256.0;

                    pixely = y * -256.0;
                    pixely = pixeldiff - pixely;

                    System.out.println("Longitude: "+(int)pixelx);
                    System.out.println("Latitude: "+(int)pixely);


                    endCoords = new Coordinate((int)pixelx, (int)pixely);
                    return true;
                }
                else{
                    System.out.println("Warning map degrees are out of bounds or not entered as decimals.\n" +
                            "Please ensure that Latitude is between 0 and -30 and Longitude is between 135 and 180.");
                    return false;
                }

            } catch (Exception e) {
                System.out.println("Warning: Enter coordinates as decimal values only.");
                scan.nextLine();
                return false;
            }
        }
        else {
            int x;
            int y;
            try {
                System.out.print("X: ");
                x = scan.nextInt();
                System.out.print("Y: ");
                y = scan.nextInt();
                System.out.println("Latitude: " + y);
                System.out.println("Longitude: " + x);
                endCoords = new Coordinate(x, y);
                return true;
            } catch (Exception e) {
                System.out.println("Warning: Enter coordinates as whole numbers only.");
                scan.nextLine();
                return false;
            }
        }
    }

    public void promptForCoordOutput(){
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("\nWould you like the output coordinates to be in (P) pixels or (L) latlong?");
            coordType = scanner.next();
            if(coordType.equalsIgnoreCase("P")){
                break;
            }
            else if(coordType.equalsIgnoreCase("L")){
                break;
            }
            else{
                System.out.println("Warning: Enter 'P' for pixels or 'L' for latitude and longitude");
                scanner.nextLine();
            }
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

        System.out.println("\nLast question, please choose your output type (separate multiple choices with commas ','):\n");
        for (Integer index : outputs.keySet()) {
            System.out.println("("+index+") " + outputs.get(index));
        }
        System.out.println(); //New line

        Scanner scan = new Scanner(System.in);

        while (true) {
            try {
                String requestedOutputs = scan.nextLine();
                Boolean requestIsValid = checkOutputRequest(requestedOutputs, outputs);

                if (requestIsValid) {
                    outputClass = getOutputTypesFromRequest(requestedOutputs, outputs);
                    break;
                }
                else {
                    throw new Exception("Please only select from the given options.");
                }
            } catch (Exception ex) {
                System.out.println("Please only select from the given options. Try again:");
            }
        }
    }

    /**
     * Helper method to check if the user requested Output types in the proper format.
     * E.g., "2" or "1,3" or "1,  2,3"
     *
     * @param in The user's input
     * @return Returns true if the input is formatted correctly, else false
     */
    public boolean checkOutputRequest(String in, Map<Integer, String> outputs) {
        String[] choices = in.split(",");

        if (choices.length > outputs.size()) { //if user requested more Output types than are available
            return false;
        }
        else { //user requested a proper amount of Output types
            for (String choice : choices) {
                try {
                    int outputNum = Integer.parseInt(choice.trim());
                    if (!(outputNum <= outputs.size() && outputNum > 0)) { //if user's requested number isn't an option
                        return false;
                    }
                } catch (NumberFormatException ex) { //one of the user's choices wasn't a number
                    return false;
                }
            }
        }

        return true; //user's request passed all the checks
    }

    /**
     * Helper method to return a well-formatted String representation of
     * the requested Output type(s). Assumes that the user's input has
     * already been checked for correctness (by checkOutputRequest).
     *
     * @param in The user's well-formatted input
     * @return A well-formatted String representation of the requested Output type(s).
     */
    public String getOutputTypesFromRequest(String in, Map<Integer, String> outputs) {
        String outputTypes = "";
        String[] choices = in.split(",");

        for (String choice : choices) {
            int key = Integer.parseInt(choice.trim());
            outputTypes = outputTypes + outputs.get(key) + ",";
        }

        return outputTypes.substring(0, outputTypes.length()-1); //drop the trailing comma from the output string
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
