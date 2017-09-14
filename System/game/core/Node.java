package game.core;

public class Node
{
    protected Node[] neighbors = null;
    private int x, y;
    private int pillIndex, powerPillIndex;
    private int numNeighbors = 0;

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

    protected Node(int _x, int _y, int _pillIndex, int _powerPillIndex)
    {
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
}
