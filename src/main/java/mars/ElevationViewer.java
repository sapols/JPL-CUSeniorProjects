package mars;

import mars.map.MapElevationsViewer;

/**
 * This class houses the method which starts the MapElevationsViewer tool
 */
public class ElevationViewer {

    /**
     * Runs the MapElevationsViewer tool
     * which allows us to inspect elevations in our maps.
     * @param args unused
     */
    public static void main(String[] args) {
        MapElevationsViewer mev = new MapElevationsViewer();
        mev.promptUser();
    }

}
