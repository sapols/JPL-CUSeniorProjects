package mars.views;

import mars.coordinate.Coordinate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A JPanel containing display components and view update logic.
 */
public class MapPanel extends JPanel {

    private MapFrame frame;
    private java.util.List<? extends Coordinate> path;
    private JTextArea textArea;
    private Image backgroundImage;

    public MapPanel(MapFrame f, java.util.List<? extends Coordinate> p, Image img) {
        frame = f;
        path = p;
        backgroundImage = img;

        this.setLayout(new BorderLayout(10, 10));

        //Create components
        textArea = new JTextArea();
        textArea.setOpaque(false);
        textArea.setText(path.toString());

        //Add components to the panel
        this.add(textArea, BorderLayout.SOUTH);
        this.setOpaque(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //TODO: DO NOT redraw maps so often...
        g.drawImage(backgroundImage, 0, 0, this); //Sets the background image like a dumbass.
    }

}
