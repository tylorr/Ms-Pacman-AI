package game.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import game.models.Node;

public class _Node implements Node
{
    protected _Node[] neighbors = null;
    private int x, y;
    private int pillIndex, powerPillIndex;
    private int numNeighbors = 0;
    private _Maze maze;

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isJunction() { return numNeighbors > 2; }
    public boolean isPill() { return pillIndex <= 0; }
    public boolean isPowerPill() { return powerPillIndex <= 0; }

    public int getNumNeighbors() { return numNeighbors; }
    public List<Node> getNeighbors() { return Arrays.asList(Arrays.copyOf(neighbors, neighbors.length)); }

    //Returns the neighbor of node index that corresponds to direction. In the case of neutral, the
    //same node index is returned
    public Node getNeighbor(int inDirection)
    {
        if(inDirection < 0 || inDirection > 3) //this takes care of "neutral"
            return this;
        else
            return neighbors[inDirection];
    }

    public int getNextDir(Node to, boolean approach) { return getNextDir(to, approach, true, 0); }

    public int getNextDir(Node to, boolean approach, boolean canReverse, int direction)
    {
        _Node[] options = Arrays.copyOf(neighbors, neighbors.length);

        if (!canReverse)
            options[direction] = null;

        int dir = -1;

        double min = Integer.MAX_VALUE;
        double max = -Integer.MAX_VALUE;

        for(int i = 0; i < options.length; i++)
        {
            if(options[i] != null)
            {
                double dist = 0;
                dist = options[i].getPathDistance(to);

                if(approach && dist < min)
                {
                    min = dist;
                    dir = i;
                }

                if(!approach && dist > max)
                {
                    max = dist;
                    dir = i;
                }
            }
        }

        return dir;
    }

    public List<Node> getPath(Node to) { return getPath(to, true, 0); }

    //Returns the path of adjacent nodes from one node to another, including these nodes
    //E.g., path from a to c might be [a,f,r,t,c]
    public List<Node> getPath(Node to, boolean canReverse, int direction)
    {
        if(getNumNeighbors()==0)
            return new ArrayList<Node>();

        _Node currentNode = this;
        ArrayList<_Node> path = new ArrayList<_Node>();

        while(currentNode != to)
        {
            path.add(currentNode);
            direction = getNextDir(to, true, canReverse, direction);
            currentNode = neighbors[direction];
        }

        Node[] arrayPath = new _Node[path.size()];

        for(int i=0;i<arrayPath.length;i++)
            arrayPath[i]=path.get(i);

        return Arrays.asList(arrayPath);
    }

    public int getPathDistance(Node to)
    {
        return maze.distances.get(this, to);
    }

    //Returns the EUCLEDIAN distance between two nodes in the current maze.
    public double getEuclideanDistance(Node to)
    {
        int dx = x - to.getX();
        int dy = y - to.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    //Returns the MANHATTAN distance between two nodes in the current maze.
    public int getManhattanDistance(Node to)
    {
        return (int)(Math.abs(x - to.getX()) + Math.abs(y - to.getY()));
    }

    protected _Node(int _x, int _y, int _pillIndex, int _powerPillIndex, _Maze _maze)
    {
        x = _x;
        y = _y;
        pillIndex = _pillIndex;
        powerPillIndex = _powerPillIndex;
        maze = _maze;
    }

    protected void setNeighbors(_Node[] _neighbors)
    {
        neighbors = _neighbors;
        numNeighbors = 0;

        for (int index = 0; index < _neighbors.length; index++)
            if (_neighbors[index] != null)
                numNeighbors++;
    }

    protected int getPillIndex()
    {
        return pillIndex;
    }
    protected int getPowerPillIndex()
    {
        return powerPillIndex;
    }
}
