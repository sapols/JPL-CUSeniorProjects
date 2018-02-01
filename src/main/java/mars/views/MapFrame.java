package mars.views;

import mars.coordinate.Coordinate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * A JFrame which holds and displays everything (the map with a path drawn on it, for now).
 */
public class MapFrame extends JFrame {

    //TODO: map path for background needs to be passed in, not hard-coded
    public MapFrame(java.util.List<? extends Coordinate> path, String mapImagePath) {
        try {
            setTitle("Algorithm Path Viewer");
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            setSize((int)(screenSize.width/2.2), (int)(screenSize.height/1.7)); //These dimensions could change
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //Add background image
            /* BufferedImage backgroundImage = ImageIO.read(new File("./background.jpg")); */ //Works in .exe package if background.jpg is in the same folder as .exe
            BufferedImage backgroundImage = ImageIO.read(new File(mapImagePath)); //Works in development
            this.getContentPane().add(new MapPanel(this, path, backgroundImage));
            setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
