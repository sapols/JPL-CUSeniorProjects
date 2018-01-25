package mars.coordinate;

/**
 * Wrapper class for an (X, Y) coordinate with extra features (visited, neighbor functions) for Greedy
 */
public class GreedyCoordinate extends Coordinate {
    private boolean visited;

    public GreedyCoordinate(int x, int y){
        super(x,y);
        visited = false;
    }

    public GreedyCoordinate(Coordinate coord){
        super(coord.x,coord.y);
        visited = false;
    }

    public Coordinate getCoordinate() { return new Coordinate(x,y); }

    //neighbor getters
    public GreedyCoordinate getNorthNeighbor() { return new GreedyCoordinate(x,y-1); }
    public GreedyCoordinate getEastNeighbor() { return new GreedyCoordinate(x+1,y); }
    public GreedyCoordinate getSouthNeighbor() { return new GreedyCoordinate(x,y+1); }
    public GreedyCoordinate getWestNeighbor() { return new GreedyCoordinate(x-1,y); }

    //visited getter
    public boolean isVisited() { return visited; }

    //visited setter
    public void setVisited(boolean _visited){ visited = _visited; }

}
