package mars;

import mars.coordinate.Coordinate;
import mars.ui.TerminalInterface;

/**
 * This class houses the main method which starts our program.
 */
public class MARS {

    /**
     * Main method to be called to start the program.
     * @param args unused
     */
    public static void main(String[] args) {
        TerminalInterface ti = new TerminalInterface();


        for( int i = 0; i < args.length; i++) {
            if(args[i].compareTo("-s") == 0){
                try{
                    ti.slope = Double.parseDouble(args[i+1]);
                }catch(Exception e){
                    System.out.println("Warning: invalid input for slope");
                }
            }
            if(args[i].compareTo("-m") == 0){ //flag for map path
                try{
                    ti.mapPath = args[i+1];
                }catch(Exception e){
                    System.out.println("Warning: Invalid input for mapPath");
                }
            }
            if(args[i].compareTo("-sc") == 0){
                try{
                    ti.startCoords = new Coordinate(Integer.parseInt(args[i+1]),Integer.parseInt(args[i+2]));
                    i = i+2;
                }catch (Exception e) {
                    System.out.println("Warning: Invalid input(s) for Start Coordinates");
                }
            }
            if(args[i].compareTo("-ec") == 0){
                try{
                    ti.endCoords = new Coordinate(Integer.parseInt(args[i+1]), Integer.parseInt(args[i+2]));
                    i = i + 2;
                }catch(Exception e){
                    System.out.println("Warning: Invalid input(s) for End Coordinates");
                }
            }
            if(args[i].compareTo("-fov") == 0){
                try{
                    ti.fieldOfView = Double.parseDouble(args[i+1]);
                }catch(Exception e){
                    System.out.println("Warning: Invalid Field of View input");
                }
            }
            if(args[i].compareTo("-a") == 0){
                try {
                    ti.algorithmClass = args[i + 1];
                }catch(Exception e){
                    System.out.println("Warning: Invalid input for algorithm class");
                }
            }
            if(args[i].compareTo("-cop") == 0){
                    ti.coordType = "P";
            }
            if(args[i].compareTo("-col") == 0){
                ti.coordType = "L";
            }
            if(args[i].compareTo("-o") == 0){
                try{
                    ti.outputClass = args[i+1];
                }catch(Exception e){
                    System.out.println("Warning: Invalid input for output class");
                }
            }


        }
        ti.promptUser();
    }

}
