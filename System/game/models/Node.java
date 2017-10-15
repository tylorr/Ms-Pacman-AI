package game.models;
import java.util.List;

public interface Node
{
    public int getX();
    public int getY();

    public boolean isPill();
    public boolean isPowerPill();
    public boolean isJunction();

    public int getNumNeighbors();
    public Node getNeighbor(int inDirection);
    public List<Node> getNeighbors();

    public int getNextDir(Node to, boolean approach);
    public int getNextDir(Node to, boolean approach, boolean canReverse, int direction);

    public List<Node> getPath(Node to);
    public List<Node> getPath(Node to, boolean canReverse, int direction);

    public int getPathDistance(Node to);
    public double getEuclideanDistance(Node to);
    public int getManhattanDistance(Node to);

    static int getReverse(int direction)
    {
        switch(direction)
        {
            case 0: return 2;
            case 1: return 3;
            case 2: return 0;
            case 3: return 1;
        }
        return 4;
    }
}
