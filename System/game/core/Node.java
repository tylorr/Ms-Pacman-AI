package game.core;

public class Node
{
    private int x, y;
    private int nodeIndex, pillIndex, powerPillIndex;
    private int numNeighbors = 0;
    protected Node[] neighbors = null;

    protected Node(int _nodeIndex, int _x, int _y, int _pillIndex, int _powerPillIndex)
    {
        nodeIndex = _nodeIndex;
        x = _x;
        y = _y;
        pillIndex = _pillIndex;
        powerPillIndex = _powerPillIndex;
    }

    public void setNeighbors(Node[] _neighbors)
    {
        neighbors = _neighbors;
        numNeighbors = 0;

        for (int index = 0; index < _neighbors.length; index++)
            if (_neighbors[index] != null)
                numNeighbors++;
    }

    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getPillIndex()
    {
        return pillIndex;
    }
    public int getPowerPillIndex()
    {
        return powerPillIndex;
    }
    public int getNumNeighbors()
    {
        return numNeighbors;
    }
    public int getNodeIndex()
    {
        return nodeIndex;
    }
}
