package game.core;

public class Enemy extends Actor
{
    protected int edibleTime, lairTime;

    protected Enemy(Node location, int direction, int _lairTime)
    {
        super(location, direction);
        edibleTime = 0;
        lairTime = _lairTime;
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

    public Node[] getPossibleLocations()
    {
        Node[] newLocations = location.getNeighbors();
        newLocations[getReverse(direction)] = null;
        return newLocations;
    }

    /*		copy.curEnemyLocs =Arrays.copyOf(curEnemyLocs, curEnemyLocs.length);
		copy.lastEnemyDirs =Arrays.copyOf(lastEnemyDirs, lastEnemyDirs.length);
		copy.edibleTimes=Arrays.copyOf(edibleTimes,edibleTimes.length);
		copy.lairTimes=Arrays.copyOf(lairTimes,lairTimes.length);*/

    protected Enemy clone()
    {
        return (Enemy)super.clone();
    }
}
