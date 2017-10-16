package org.geotools;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;

import java.awt.image.Raster;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GeoTIFF {
    private static GridCoverage2D grid;
    private static Raster gridData;

    public static void main(String fileLocation) throws Exception{
        initTif(fileLocation);
    }

    private static void initTif(String fileLocation) throws Exception{
        File tiffFile = new File(fileLocation);
        GeoTiffReader reader = new GeoTiffReader(tiffFile);

        grid = reader.read(null);
        gridData = grid.getRenderedImage().getData();
    }

    public static double getValue(double x, double y) throws Exception{
        GridGeometry2D gg = grid.getGridGeometry();

        DirectPosition2D posWorld = new DirectPosition2D(x,y);
        GridCoordinates2D posGrid = gg.worldToGrid(posWorld);

        //envelope is the size in the target projection
        double[] pixel = new double[1];
        double[] data = gridData.getPixel(posGrid.x,posGrid.y,pixel);
        return data[0];
    }
}