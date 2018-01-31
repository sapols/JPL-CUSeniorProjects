package mars.coordinate;

import java.util.ArrayList;

/**
 * Wrapper class for an (X, Y) coordinate with extra features (visited, neighbor functions) for Greedy
 */
public class GreedyCoordinate extends Coordinate {
    private static final int const_SE = 45;
    private static final int const_SW = 135;
    private static final int const_NW = 225;
    private static final int const_NE = 315;
    private static final int const_E = 0;
    private static final int const_S = 90;
    private static final int const_W = 180;
    private static final int const_N = 270;

    private boolean visited;
    private int direction;

    public GreedyCoordinate(int x, int y){
        super(x,y);
        visited = false;
    }

    public GreedyCoordinate(Coordinate coord){
        super(coord.x,coord.y);
        visited = false;
    }

    public GreedyCoordinate(int x, int y, int _direction){
        super(x,y);
        visited = false;
        direction = _direction;
    }

    public Coordinate getCoordinate() { return new Coordinate(x,y); }

    //neighbor getter
    public ArrayList<GreedyCoordinate> getNeighbors(){
        ArrayList<GreedyCoordinate> output = new ArrayList<GreedyCoordinate>();
        output.add(new GreedyCoordinate(x,y-1,const_N)); //n
        output.add(new GreedyCoordinate(x+1,y,const_E)); //e
        output.add(new GreedyCoordinate(x,y+1,const_S)); //s
        output.add(new GreedyCoordinate(x-1,y,const_W)); //w
        output.add(new GreedyCoordinate(x-1,y-1,const_NW)); //nw
        output.add(new GreedyCoordinate(x-1,y+1,const_SW)); //sw
        output.add(new GreedyCoordinate(x+1,y-1,const_NE)); //ne
        output.add(new GreedyCoordinate(x+1,y+1,const_SE)); //se
        return output;
    }

    //visited getter
    public boolean isVisited() { return visited; }
    public int getDirection() { return direction; }

    //visited setter
    public void setVisited(boolean _visited){ visited = _visited; }

}
