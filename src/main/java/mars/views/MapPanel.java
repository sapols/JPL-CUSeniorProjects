package mars.views;

import mars.coordinate.Coordinate;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * A JPanel containing map display components and update logic.
 */
public class MapPanel extends JPanel {

    private MapFrame frame;
    private java.util.List<? extends Coordinate> path;
    private BufferedImage mapBackgroundImage;

    /**
     * Constructor for this JPanel subclass.
     * It calls helper methods to scale the map image and draw a path on it.
     *
     * @param f The MapFrame which holds this panel
     * @param p The path output by our rover's algorithm
     * @param img The image of the map used by our rover
     */
    public MapPanel(MapFrame f, java.util.List<? extends Coordinate> p, BufferedImage img) {
        frame = f;
        path = p;
        mapBackgroundImage = img;

        setPreferredSize(new Dimension(mapBackgroundImage.getWidth(), mapBackgroundImage.getHeight()));
        this.setLayout(new BorderLayout(10, 10));

        //Create components
        mapBackgroundImage = convertColorModel(mapBackgroundImage, BufferedImage.TYPE_INT_ARGB); //Needed to add non-greyscale colors
        convertPathXYtoJavaXY(); //Needed to account for differences in coordinate systems
        drawPathOnImage(mapBackgroundImage, path);
    }

    /**
     * Converts the color model of the given BufferedImage to the given type.
     * This is primarily used to convert from a greyscale color model to one
     * that allows color.
     *
     * @return The given image but with the updated color model.
     */
    public static BufferedImage convertColorModel(BufferedImage src, int bufImgType) {
        BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), bufImgType);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(src, 0, 0, null); //Transfer old image into the new one with correct color model
        g2d.dispose();
        return img;
    }

    /**
     * Account for the disparity between Geotools' coordinate system and the one
     * used by Java images. In Java images, X grows normally but Y grows downward.
     * So convert the "standard" (X,Y)'s in the path to the corresponding (X,Y)'s
     * in Java image speak.
     */
    public void convertPathXYtoJavaXY() {
        for (Coordinate c : path) {
            int y = c.getY();
            int javaY = mapBackgroundImage.getHeight() - y;
            c.setY(javaY);
        }
    }

    /**
     * Reverse the disparity between Geotools' coordinate system and the one
     * used by Java images. In Java images, X grows normally but Y grows downward.
     * So convert the Java (X,Y)'s in the path back to the corresponding original (X,Y)'s.
     */
    public static void convertJavaXYtoPathXY(java.util.List<? extends Coordinate> path, BufferedImage mapBackgroundImage) {
        for (Coordinate c : path) {
            int javaY = c.getY();
            int y = mapBackgroundImage.getHeight() - javaY;
            c.setY(y);
        }
    }

    /**
     * Given an image of a map and a path of Coordinates,
     * draw that path on the map as a red line.
     *
     * @param map The map image.
     * @param path The path of Coordinates to be drawn on the map.
     */
    public void drawPathOnImage(BufferedImage map, java.util.List<? extends Coordinate> path) {
        for (Coordinate c : path) {
            int x = c.getX();
            int y = c.getY();
            map.setRGB(x, y, new Color(255, 0, 18, 187).getRGB());
        }
    }

    /**
     * Refreshes the components on panel.
     * Overriding "paintComponent" is the idiomatic way to update views in Java Swing.
     *
     * @param g The Graphics object of this component
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //TODO: Add buttons for zooming in/out.
        //TODO: DO NOT redraw maps so often... (find a good solution to this)
        g.drawImage(mapBackgroundImage, 0, 0, this); //Sets the background image, not scaled (so zoomed in).
//        g.drawImage(mapBackgroundImage, 0, 0, this.getWidth(), this.getHeight(), this); //Sets the background image, scaled to fit view.
    }

}
