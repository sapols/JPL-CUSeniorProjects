package org.geotools;

import mars.IGeoTiff;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoKeyEntry;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;

import java.awt.image.Raster;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.in;

public class GeoTIFF implements IGeoTiff {
    private static GridCoverage2D grid;
    private static Raster gridData;

    public void main(String fileLocation) throws Exception { //initialization
        initTif(fileLocation);
    }

    public void initTif(String fileLocation) throws Exception {
        File tiffFile = new File(fileLocation); //get the tiff
        GeoTiffReader reader = new GeoTiffReader(tiffFile); //make a GeoTiffReader (a apache geotools class)

        /*String geokey_value = "";
        GeoTiffIIOMetadataDecoder metadata = reader.getMetadata();
        Collection<GeoKeyEntry> geokeys = metadata.getGeoKeys();
        for (GeoKeyEntry geokey : geokeys){
            geokey_value = Integer.toString(geokey.getKeyID()) + " = ";
            for(Integer keyval : (geokey.getValues())){
                geokey_value += Integer.toString(keyval) + " ";
            }
            System.out.println(geokey_value);
        }*/

        grid = reader.read(null); //read in the tiff file
        gridData = grid.getRenderedImage().getData(); //and its data
    }

    public double getValue(double x, double y) throws Exception { //take in x,y and return elevation
        GridGeometry2D gg = grid.getGridGeometry();

        DirectPosition2D posWorld = new DirectPosition2D(x,y);
        GridCoordinates2D posGrid = gg.worldToGrid(posWorld);

        //envelope is the size in the target projection
        double[] pixel = new double[1];
        double[] data = gridData.getPixel(posGrid.x,posGrid.y,pixel);
        return data[0];
    }
}