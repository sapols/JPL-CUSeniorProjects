package mars.views;

import mars.coordinate.Coordinate;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A JFrame which holds and displays everything (just a JPanel with the map and a path drawn on it, for now).
 */
public class MapFrame extends JFrame {

    int frameWidth;
    int frameHeight;
    BufferedImage backgroundImage;
    java.util.List<? extends Coordinate> path;

    /**
     * Constructor for this JFrame subclass.
     * It sets the frame's title, the size of the frame as a specific fraction of the screen size,
     * and declares that the program should end on hitting the close button.
     * It then loads the map image used by our rover and gives it to the JPanel
     * which manipulates and displays it.
     *
     * @param path The path which was output by our rover's algorithm
     * @param mapImagePath The file path to the map used by our rover
     */
    public MapFrame(java.util.List<? extends Coordinate> path, String mapImagePath) {
        try {
            setTitle("MARS Path Viewer");
            this.path = path;
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            //TODO: maybe set size dimensions based on aspect ratio of map image?
            frameWidth = (int)(screenSize.width/2.2);
            frameHeight = (int)(screenSize.height/1.7);
            setSize(frameWidth, frameHeight);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //Add the (scrollable) map image to this frame
            try {
                //Try to use .jpg version instead of .tif(f) to greatly shrink memory use
                String jpgImagePath = mapImagePath.substring(0, mapImagePath.indexOf('.')) + ".jpg";
                backgroundImage = ImageIO.read(new File(jpgImagePath));
            } catch (IOException ex) {
                //Default to the actual GeoTIFF image
                backgroundImage = ImageIO.read(new File(mapImagePath));
            }
            MapPanel imagePanel = new MapPanel(this, path, backgroundImage);
            this.getContentPane().add(new ImageScroller(imagePanel));
            setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * A JScrollPane which allows us to scroll around large images.
     * For convenience, it will load scrolled to the first (X,Y) coordinate in "path".
     */
    private class ImageScroller extends JScrollPane {
        private ImageScroller(MapPanel imagePanel) {
            super();

            setPreferredSize(new Dimension(frameWidth, frameHeight));
            getViewport().add(imagePanel); //Put the pane with the map image in this scroll pane

            int firstX = path.get(0).getX();
            int firstY = path.get(0).getY(); //Note that all the Y's in "path" have been altered by MapPanel.convertPathXYtoJavaXY()

            //Center the view position as much as possible
            if (firstX < frameWidth/2)
                firstX = 0;
            else
                firstX = firstX - frameWidth/2;

            if (firstY < frameHeight/2)
                firstY = 0;
            else
                firstY = firstY - frameHeight/2;

            getViewport().setViewPosition(new Point(firstX, firstY));
            //Put the altered path back to normal to avoid unwanted changes to the algorithm's path
            MapPanel.convertJavaXYtoPathXY(path, backgroundImage);
        }
    }

}
