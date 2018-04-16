package mars.views;

import mars.coordinate.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A JPanel containing map display components and update logic.
 */
public class MapCoordinatePickerPanel extends JPanel {

    private MapCoordinatePickerFrame frame;
    public BufferedImage mapBackgroundImage;

    /**
     * Constructor for this JPanel subclass.
     * It calls helper methods to scale the map image and draw a path on it.
     *
     * @param f The MapFrame which holds this panel
     * @param img The image of the map used by our rover
     */
    public MapCoordinatePickerPanel(MapCoordinatePickerFrame f, BufferedImage img) {
        frame = f;
        mapBackgroundImage = img;

        setPreferredSize(new Dimension(mapBackgroundImage.getWidth(), mapBackgroundImage.getHeight()));
        this.setLayout(new BorderLayout(10, 10));

        //Create components
        mapBackgroundImage = convertColorModel(mapBackgroundImage, BufferedImage.TYPE_INT_ARGB); //Needed to add non-greyscale colors
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
