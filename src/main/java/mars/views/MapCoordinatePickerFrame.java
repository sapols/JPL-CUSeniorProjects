package mars.views;

import mars.coordinate.Coordinate;
import mars.ui.TerminalInterface;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A JFrame which holds and displays everything (just a JPanel with the map and a path drawn on it, with a MouseListener).
 */
public class MapCoordinatePickerFrame extends JFrame {

    int frameWidth;
    int frameHeight;
    BufferedImage backgroundImage;
    MapCoordinatePickerPanel imagePanel;
    TerminalInterface ti;
    private int numClicks = 0;
    Coordinate startCoord = new Coordinate(0, 0);
    Coordinate endCoord = new Coordinate(0, 0);

    /**
     * Constructor for this JFrame subclass.
     *
     * Its purpose is to let the user click their map for start and end coordinates.
     *
     * It sets the frame's title, the size of the frame as a specific fraction of the screen size,
     * and declares that the program should end on hitting the close button.
     * It then loads the map image used by our rover and gives it to the JPanel
     * which manipulates and displays it.
     *
     * @param ui The TerminalInterface which opened this frame
     * @param mapImagePath The file path to the map used by our rover
     */
    public MapCoordinatePickerFrame(TerminalInterface ui, String mapImagePath) {
        try {
            setTitle("Click to select your START coordinates:");
            ti = ui;
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
            imagePanel = new MapCoordinatePickerPanel(this, backgroundImage);

            /**
             * Capture the first click's coordinates as "startCoord"
             * and the second click's coordinates as "endCoord".
             */
            imagePanel.addMouseListener(new MouseListener() {
                //support clicking to get start/end coords
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = backgroundImage.getHeight() - e.getY(); //y here should count from the bottom, not the top
                    int imageY = e.getY();

                    if (numClicks == 0) { //First click
                        startCoord.setX(x);
                        startCoord.setY(y);
                        ti.startCoords = startCoord;
                        drawStartMarkOnMap(x, imageY, 10);
                        setTitle("Click to select your END coordinates:");
                        numClicks++;
                    }
                    else if (numClicks == 1) { //Second click
                        endCoord.setX(x);
                        endCoord.setY(y);
                        ti.endCoords = endCoord;
                        setTitle("Coordinates accepted.");
                        drawEndMarkOnMap(x, imageY, 10);

                        try {
                            Thread.sleep(2000); //Wait one second after click to close the frame
                            closeThisFrame();
                        } catch (InterruptedException ex) {
                            closeThisFrame();
                        }
                    }
                }

                public void mousePressed(MouseEvent e) {
                    //no-op
                }
                public void mouseReleased(MouseEvent e) {
                    //no-op
                }
                public void mouseEntered(MouseEvent e) {
                    //no-op
                }
                public void mouseExited(MouseEvent e) {
                    //no-op
                }
            });

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
        private ImageScroller(MapCoordinatePickerPanel imagePanel) {
            super();

            setPreferredSize(new Dimension(frameWidth, frameHeight));
            getViewport().add(imagePanel); //Put the pane with the map image in this scroll pane

            int firstX = 0;
            int firstY = 0;

            //Center the view position as much as possible, given firstX and firstY
            if (firstX < frameWidth/2)
                firstX = 0;
            else
                firstX = firstX - frameWidth/2;

            if (firstY < frameHeight/2)
                firstY = 0;
            else
                firstY = firstY - frameHeight/2;

            getViewport().setViewPosition(new Point(firstX, firstY));
        }
    }

    /**
     * Draw a square around the given x,y coords.
     *
     * @param x X component of the location on the map to get marked
     * @param y Y component of
     */
    public void drawStartMarkOnMap(int x, int y, int r) {
        int leftXBorder = x-(r/2);
        int rightXBorder = x+(r/2);
        int topYBorder = y-(r/2);
        int bottomYBorder = y+(r/2);

        for (int i = leftXBorder; i <= rightXBorder; i++) {
            for (int j = topYBorder; j <= bottomYBorder; j++) {
                if (i == leftXBorder || i == rightXBorder || j == topYBorder || j == bottomYBorder) {
                    imagePanel.mapBackgroundImage.setRGB(i, j, new Color(255, 0, 18, 187).getRGB());
                }
            }
        }

        imagePanel.repaint();
    }

    /**
     * Draw a circle around the given x,y coords.
     *
     * @param x X component of the location on the map to get marked
     * @param y Y component of
     */
    public void drawEndMarkOnMap(int x, int y, int r) {
        imagePanel.repaint();
        x = x-(r/2);
        y = y-(r/2);
        Graphics g = imagePanel.getGraphics();
        g.setColor(new Color(255, 0, 18, 187));
        g.drawOval(x,y,r,r);

        imagePanel.repaint();
    }

    /**
     * Closes this JFrame without ending program execution
     */
    public void closeThisFrame() {
        this.dispose();
    }

}