package game.core;

public class Enemy extends Actor
{
    protected Enemy(Node location, int direction)
    {
        super(location, direction);
    }

    public int[] getPossibleDirs()
    {
        int numNeighbors = location.getNumNeighbors();

        if (numNeighbors == 0)
            return new int[0];

        Node[] nodes = location.neighbors;
        int[] directions;

        directions = new int[numNeighbors - 1];

        int index = 0;

        for (int i = 0; i < nodes.length; i++)
            if (nodes[i] != null)
            {
                if (i != getReverse(direction))
                    directions[index++] = i;
            }

        return directions;
    }
}
