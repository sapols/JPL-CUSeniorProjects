package mars.views;

import mars.coordinate.Coordinate;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

    //-----Curiosity Path Stuff-----------------------------------------------------------------------------------------

    /**
     * Main method to test Curiosity path stuff
     * @param args unused
     */
//    public static void main(String[] args) {
//        System.out.println("Hola mundo, soy el Curiosity. Esta es mi ruta.\n");
//
//        //TODO: add all pixel coords from map
//        Coordinate[] pixels = { new Coordinate(2194, 171),
//                new Coordinate(2206, 177),
//                new Coordinate(2210, 179),
//                new Coordinate(2216, 183),
//                new Coordinate(2239, 194),
//                new Coordinate(2250, 186),
//                new Coordinate(2263, 196),
//                new Coordinate(2282, 192),
//                new Coordinate(2304, 188),
//                new Coordinate(2316, 183),
//                new Coordinate(2325, 175),
//                new Coordinate(2329, 172),
//                new Coordinate(2327, 182),
//                new Coordinate(2326, 196),
//                new Coordinate(2318, 192),
//                new Coordinate(2298, 190),
//                new Coordinate(2285, 208),
//                new Coordinate(2270, 216),
//                new Coordinate(2262, 222),
//                new Coordinate(2245, 237),
//                new Coordinate(2226, 250),
//                new Coordinate(2208, 255),
//                new Coordinate(2192, 264),
//                new Coordinate(2174, 290),
//                new Coordinate(2127, 300),
//                new Coordinate(2117, 328),
//                new Coordinate(2070, 356),
//                new Coordinate(2030, 354),
//                new Coordinate(2020, 380),
//                new Coordinate(1980, 416),
//                new Coordinate(1966, 452),
//                new Coordinate(1949, 464),
//                new Coordinate(1922, 490),
//                new Coordinate(1912, 533),
//                new Coordinate(1889, 543),
//                new Coordinate(1874, 568),
//                new Coordinate(1868, 598),
//                new Coordinate(1853, 640),
//                new Coordinate(1820, 664),
//                new Coordinate(1785, 701),
//                new Coordinate(1763, 718),
//                new Coordinate(1738, 716),
//                new Coordinate(1704, 746),
//                new Coordinate(1681, 776),
//                new Coordinate(1660, 791),
//                new Coordinate(1640, 783),
//                new Coordinate(1630, 786),
//                new Coordinate(1592, 791),
//                new Coordinate(1570, 804),
//                new Coordinate(1534, 878),
//                new Coordinate(1511, 883),
//                new Coordinate(1514, 910),
//                new Coordinate(1560, 942),
//                new Coordinate(1572, 964),
//                new Coordinate(1557, 982),
//                new Coordinate(1547, 1008),
//                new Coordinate(1505, 1005),
//                new Coordinate(1450, 1017),
//                new Coordinate(1432, 1010),
//                new Coordinate(1406, 1024),
//                new Coordinate(1368, 1038),
//                new Coordinate(1336, 1060),
//                new Coordinate(1314, 1094),
//                new Coordinate(1306, 1110),
//                new Coordinate(1276, 1129),
//                new Coordinate(1264, 1156),
//                new Coordinate(1260, 1204),
//                new Coordinate(1265, 1244),
//                new Coordinate(1274, 1254),
//                new Coordinate(1254, 1286),
//                new Coordinate(1256, 1301),
//                new Coordinate(1244, 1328),
//                new Coordinate(1229, 1316),
//                new Coordinate(1209, 1310),
//                new Coordinate(1192, 1317),
//                new Coordinate(1159, 1320),
//                new Coordinate(1146, 1345),
//                new Coordinate(1160, 1370),
//                new Coordinate(1165, 1381),
//                new Coordinate(1142, 1402),
//                new Coordinate(1115, 1426),
//                new Coordinate(1094, 1425),
//                new Coordinate(1058, 1438),
//                new Coordinate(1057, 1460),
//                new Coordinate(1040, 1444),
//                new Coordinate(1042, 1457),
//                new Coordinate(1034, 1454),
//                new Coordinate(1030, 1469),
//                new Coordinate(1022, 1492),
//                new Coordinate(1022, 1506),
//                new Coordinate(1033, 1519),
//                new Coordinate(1025, 1537),
//                new Coordinate(1008, 1553),
//                new Coordinate(989, 1576),
//                new Coordinate(972, 1594),
//                new Coordinate(975, 1625),
//                new Coordinate(963, 1644),
//                new Coordinate(976, 1644),
//                new Coordinate(989, 1638),
//                new Coordinate(994, 1658),
//                new Coordinate(1004, 1654),
//                new Coordinate(994, 1658),
//                new Coordinate(989, 1638),
//                new Coordinate(976, 1644),
//                new Coordinate(956, 1623),
//                new Coordinate(935, 1613),
//                new Coordinate(915, 1630),
//                new Coordinate(886, 1628),
//                new Coordinate(862, 1623),
//                new Coordinate(836, 1632),
//                new Coordinate(844, 1660),
//                new Coordinate(830, 1693),
//                new Coordinate(832, 1718),
//                new Coordinate(818, 1723),
//                new Coordinate(803, 1738),
//                new Coordinate(787, 1761),
//                new Coordinate(776, 1783),
//                new Coordinate(766, 1805),
//                new Coordinate(757, 1833),
//                new Coordinate(769, 1850),
//                new Coordinate(756, 1874),
//                new Coordinate(766, 1893),
//                new Coordinate(758, 1902),
//                new Coordinate(785, 1930),
//                new Coordinate(786, 1953),
//                new Coordinate(790, 1975),
//                new Coordinate(805, 1995),
//                new Coordinate(808, 2019),
//                new Coordinate(801, 2045),
//                new Coordinate(811, 2070),
//                new Coordinate(834, 2093),
//                new Coordinate(847, 2106),
//                new Coordinate(850, 2130),
//                new Coordinate(852, 2148),
//                new Coordinate(826, 2161),
//                new Coordinate(836, 2145),
//                new Coordinate(855, 2168),
//                new Coordinate(864, 2176),
//                new Coordinate(852, 2189),
//                new Coordinate(860, 2200),
//                new Coordinate(887, 2195),
//                new Coordinate(878, 2198),
//                new Coordinate(882, 2227),
//                new Coordinate(894, 2246),
//                new Coordinate(900, 2260),
//                new Coordinate(911, 2276),
//                new Coordinate(925, 2278),
//                new Coordinate(944, 2289),
//                new Coordinate(949, 2316),
//                new Coordinate(967, 2324),
//                new Coordinate(994, 2318),
//                new Coordinate(1007, 2314),
//                new Coordinate(1029, 2318),
//                new Coordinate(1040, 2334),
//                new Coordinate(1052, 2327),
//                new Coordinate(1083, 2342),
//                new Coordinate(1076, 2357),
//                new Coordinate(1084, 2367),
//                new Coordinate(1077, 2382),
//                new Coordinate(1082, 2391),
//                new Coordinate(1089, 2384),
//                new Coordinate(1100, 2392),
//                new Coordinate(1096, 2409),
//                new Coordinate(1108, 2416),
//                new Coordinate(1107, 2437),
//                new Coordinate(1122, 2437),
//                new Coordinate(1125, 2446),
//                new Coordinate(1134, 2449),
//                new Coordinate(1138, 2465),
//                new Coordinate(1152, 2456),
//                new Coordinate(1160, 2444),
//                new Coordinate(1163, 2427),
//                new Coordinate(1170, 2426),
//                new Coordinate(1179, 2416),
//                new Coordinate(1187, 2410),
//                new Coordinate(1196, 2404),
//                new Coordinate(1198, 2396) };
//                //TODO: Note that when constructing Curiosity's path,
//                //      if two points aren't touching pixels, Greedy alg
//                //      can be used to find a path to fill in small gaps.
//
//        ArrayList<String[]> curiosityLatLon = getCuriosityPathFromImagePixels(pixels);
//
//        printCuriosityPath(curiosityLatLon);
//    }
//
//    /*
//     * According to math laid out in MARS-135, convert pixels from this image:
//     * https://marsmobile.jpl.nasa.gov/imgs/2018/03/MSL_TraverseMap_Sol2003-full.jpg
//     *
//     * into lat/lon values to get Curiosity's path across Mars from its landing date to April 2018.
//     */
//    public static ArrayList<String[]> getCuriosityPathFromImagePixels(Coordinate[] imagePixels) {
//        ArrayList<String[]> curiosityPath = new ArrayList<String[]>();
//
//        for (int i = 0; i < imagePixels.length; i++) {
//            String lat = "4º";
//            String lon = "137º";
//            int x = imagePixels[i].getX();
//            int y = imagePixels[i].getY();
//
//            String latMinutes = new Double(34.732732 + (y*0.00368392)).toString();
//            String lonMinutes  = new Double(18.404863 + (x*0.00368392)).toString();
//
//            lat = lat + latMinutes;
//            lon = lon + lonMinutes;
//            lat = lat + "'S";
//            lon = lon + "'E";
//
//            String[] latLon = { lat, lon };
//            curiosityPath.add(latLon);
//        }
//
//        return curiosityPath;
//    }
//
//    /*
//     * Print the contents of an ArrayList<String[]> that is Curiosity's path in lat/lon.
//     */
//    public static void printCuriosityPath(ArrayList<String[]> curiosityPath) {
//        for (int i = 1; i <= curiosityPath.size(); i++) {
//            String lat = curiosityPath.get(i - 1)[0].toString();
//            String lon = curiosityPath.get(i - 1)[1].toString();
//            System.out.println(i + ". (" + lat + ", " + lon + ")");
//        }
//    }

}
