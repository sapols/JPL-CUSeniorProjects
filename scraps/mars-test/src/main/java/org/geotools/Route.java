package org.geotools;

public class Route {
    private double startPoint;
    private double endPoint;
    private GeoTIFF terrain;

    public void main(String[] args) throws Exception {
        terrain = new GeoTIFF();
        terrain.main(args[0]);
    }

    public void setStartPoint(double newStart){
        startPoint = newStart;
    }

    public void setEndPoint(double newEnd){
        endPoint = newEnd;
    }

    public double getStartPoint(){
        return startPoint;
    }

    public double getEndPoint(){
        return endPoint;
    }

    public double getValue(double x, double y) throws Exception {
        return terrain.getValue(x,y);
    }

}