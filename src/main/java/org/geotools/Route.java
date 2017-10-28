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
    } //some cute little get/set functions

    public void setEndPoint(double newEnd){
        endPoint = newEnd;
    }

    public double getStartPoint(){
        return startPoint;
    }

    public double getEndPoint(){
        return endPoint;
    }

    public double getValue(double x, double y) throws Exception { //just take in stuff to play with terrain
        return terrain.getValue(x,y);
    }

    public void getLine(double x) throws Exception {
        double lastStat = -1;
        double newStat = -1;
        int lastY = 0;
        int maxY = 46080;
        for(int i=0; i<maxY; i++){
            newStat = terrain.getValue(x,i);
            if(newStat != lastStat){
                System.out.println(Double.toString(lastStat) + " (" + Integer.toString(lastY) + " - " + Integer.toString(i) + ")");
                lastStat = newStat;
                lastY = i;
            }
        }
    }

}