package game.core;

public class Hero extends Actor
{
    protected Hero(Node location, int direction)
    {
        super(location, direction);
    }

    public int[] getPossibleDirs(boolean includeReverse)
    {
        int numNeighbors = location.getNumNeighbors();

        if (numNeighbors == 0)
            return new int[0];

        Node[] nodes = location.neighbors;
        int[] directions;

        if (includeReverse || (direction < 0 || direction > 3))
            directions = new int[numNeighbors];
        else
            directions = new int[numNeighbors - 1];

        int index = 0;

        for (int i = 0; i < nodes.length; i++)
            if (nodes[i] != null)
            {
                if (includeReverse || (direction < 0 || direction > 3))
                    directions[index++] = i;
                else if (i != getReverse(direction))
                    directions[index++] = i;
            }

        return directions;
    }

    protected Hero clone()
    {
        return (Hero)super.clone();
    }

    public int getNextDir(Node to, boolean closer)
    {
        return getDirFromOptions(location.neighbors, to, closer);
    }
}
